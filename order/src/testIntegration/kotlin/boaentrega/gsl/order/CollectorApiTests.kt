package boaentrega.gsl.order

import boaentrega.gsl.order.domain.collector.PickupRequest
import boaentrega.gsl.order.domain.collector.PickupRequestRepository
import boaentrega.gsl.order.domain.collector.PickupRequestService
import boaentrega.gsl.order.domain.collector.PickupRequestStatus
import boaentrega.gsl.order.domain.collector.web.CollectorController
import boaentrega.gsl.order.support.eventsourcing.connectors.dummy.DummyProducerConnector
import boaentrega.gsl.order.support.eventsourcing.messages.Message
import boaentrega.gsl.order.support.extensions.ClassExtensions.toJsonString
import boaentrega.gsl.order.support.extensions.ClassExtensions.toObject
import boaentrega.gsl.order.support.outbox.OutboxConnectorService
import boaentrega.gsl.order.support.outbox.OutboxRepository
import gsl.schemas.FreightEvent
import gsl.schemas.FreightEventStatus
import gsl.schemas.FreightMovePackageCommand
import org.hamcrest.Matchers.hasSize
import org.jeasy.random.EasyRandom
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.CsvSource
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.MediaType
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.*
import kotlin.streams.toList

internal class CollectorApiTests : AbstractIntegrationTest() {

    @Autowired
    private lateinit var repository: PickupRequestRepository

    @Autowired
    private lateinit var service: PickupRequestService

    @Autowired
    private lateinit var outboxRepository: OutboxRepository

    @Autowired
    private lateinit var outboxConnectorService: OutboxConnectorService


    override fun createResource(): Any {
        return CollectorController(service)
    }

    val easyRandom = EasyRandom()
    var entities = listOf<PickupRequest>()

    @AfterEach
    fun reset() {
        repository.deleteAll()
        outboxRepository.deleteAll()
        DummyProducerConnector.registry.clear()
    }

    @BeforeEach
    fun setup() {
        val values = easyRandom.objects(PickupRequest::class.java, 200).toList()
        values.forEach { it.status = PickupRequestStatus.WAITING }
        entities = repository.saveAllAndFlush(values)
    }

    @ParameterizedTest
    @CsvSource(
            "'', true, 0, false",
            "?page=1, true, 0, false",
            "?page=2, false, 1, false",
            "?page=10, false, 9, true"
    )
    fun getAll(page: String, first: Boolean, index: Int, last: Boolean) {
        restMockMvc.perform(get("/collector$page")
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk)
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(jsonPath("\$.content", hasSize<String>(20)))
                .andExpect(jsonPath("\$.first").value(first))
                .andExpect(jsonPath("\$.number").value(index))
                .andExpect(jsonPath("\$.last").value(last))
                .andExpect(jsonPath("\$.totalPages").value(10))
                .andExpect(jsonPath("\$.numberOfElements").value(20))
                .andExpect(jsonPath("\$.size").value(20))
    }

    @Test
    fun getById() {
        val id = entities.first().id
        restMockMvc.perform(get("/collector/{id}", id)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk)
                .andExpect(jsonPath("\$.status").value(PickupRequestStatus.WAITING.toString()))
    }

    @Test
    fun markAsOutToPickupTheProduct() {
        val id = entities.first().id
        val contentMap = mapOf("employee" to "fulano de tal")
        restMockMvc.perform(put("/collector/{id}/pickup", id)
                .contentType(MediaType.APPLICATION_JSON)
                .content(contentMap.toJsonString())
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk)
                .andExpect(
                        jsonPath("\$.status").value(PickupRequestStatus.PICKUP_PROCESS.toString()))

        checkEventSourcingEvents(FreightEventStatus.COLLECTION_PICKUP_OUT)
    }

    @Test
    fun markAsTaken() {
        val id = entities.first().id
        restMockMvc.perform(put("/collector/{id}/taken", id)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk)
                .andExpect(
                        jsonPath("\$.status").value(PickupRequestStatus.TAKEN.toString()))

        checkEventSourcingEvents(FreightEventStatus.COLLECTION_PICKUP_TAKEN)
    }

    @Test
    fun markAsOnPackaging() {
        val id = entities.first().id
        val contentMap = mapOf("employee" to "fulano de tal")
        restMockMvc.perform(put("/collector/{id}/packaging", id)
                .contentType(MediaType.APPLICATION_JSON)
                .content(contentMap.toJsonString())
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk)
                .andExpect(
                        jsonPath("\$.status").value(PickupRequestStatus.ON_PACKAGING.toString()))

        checkEventSourcingEvents(FreightEventStatus.COLLECTION_PACKAGE_PREPARING)
    }

    @Test
    fun markAsReadyToStartDelivery() {
        val id = entities.first().id
        val contentMap = mapOf("address" to "fulano de tal")
        val result = restMockMvc.perform(put("/collector/{id}/ready", id)
                .contentType(MediaType.APPLICATION_JSON)
                .content(contentMap.toJsonString())
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk)
                .andExpect(
                        jsonPath("\$.status").value(PickupRequestStatus.FINISHED.toString()))
                .andReturn()

        val response = result.response

        checkEventSourcingEvents(FreightEventStatus.COLLECTION_PACKAGE_READY_TO_MOVE)
        checkEventSourcingCommands(response.contentAsString.toObject())
    }

    private fun checkEventSourcingEvents(freightStatus: FreightEventStatus) {
        releaseMessages()

        val message = DummyProducerConnector.registry.entries.first().value[0].toObject<Message>()
        val event = message.content.toObject<FreightEvent>()
        Assertions.assertEquals(freightStatus, event.status)
    }

    private fun checkEventSourcingCommands(pickupRequest: PickupRequest) {
        val message = DummyProducerConnector.registry.entries.first { it.key.contains("command") }.value[0].toObject<Message>()
        val command = message.content.toObject<FreightMovePackageCommand>()

        Assertions.assertEquals(pickupRequest.trackId, command.trackId)
        Assertions.assertEquals(pickupRequest.orderId, command.orderId)
        Assertions.assertEquals(pickupRequest.freightId, command.freightId)
        Assertions.assertEquals(pickupRequest.packageAddress, command.from)
        Assertions.assertEquals(pickupRequest.destination, command.to)
    }

    private fun releaseMessages() {
        Assertions.assertTrue(outboxRepository.getTop10ByIsPublishedFalse().isNotEmpty())
        outboxConnectorService.releaseMessages()
        Assertions.assertTrue(outboxRepository.getTop10ByIsPublishedFalse().isEmpty())
    }

}