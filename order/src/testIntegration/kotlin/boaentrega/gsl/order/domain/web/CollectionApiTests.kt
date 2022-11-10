package boaentrega.gsl.order.domain.web

import boaentrega.gsl.order.AbstractWebTest
import boaentrega.gsl.order.configuration.constants.ResourcePaths
import boaentrega.gsl.order.domain.collection.PickupRequest
import boaentrega.gsl.order.domain.collection.PickupRequestRepository
import boaentrega.gsl.order.domain.collection.PickupRequestService
import boaentrega.gsl.order.domain.collection.PickupRequestStatus
import boaentrega.gsl.order.domain.collection.web.CollectorController
import boaentrega.gsl.order.support.eventsourcing.connectors.dummy.DummyProducerConnector
import boaentrega.gsl.order.support.extensions.ClassExtensions.toJsonString
import boaentrega.gsl.order.support.extensions.ClassExtensions.toObject
import boaentrega.gsl.order.support.outbox.OutboxConnectorService
import boaentrega.gsl.order.support.outbox.OutboxRepository
import gsl.schemas.FreightEvent
import gsl.schemas.FreightEventStatus
import gsl.schemas.FreightMovePackageCommand
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.MediaType
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.status

internal class CollectionApiTests : AbstractWebTest<PickupRequest>() {

    companion object {
        const val RESOURCE = ResourcePaths.COLLECTION
    }

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

    override fun getRepository() = repository
    override fun getEntityType() = PickupRequest::class.java
    override fun preProcessing(data: List<PickupRequest>) = data.forEach { it.status = PickupRequestStatus.WAITING }
    override fun getResource() = RESOURCE

    @AfterEach
    fun reset() {
        repository.deleteAll()
        outboxRepository.deleteAll()
        DummyProducerConnector.clearMessages()
    }

    @Test
    fun checkStatusById() {
        val id = entities.first().id
        restMockMvc.perform(get("$RESOURCE/{id}", id))
                .andExpect(jsonPath("\$.status").value(PickupRequestStatus.WAITING.toString()))
    }

    @Test
    fun markAsOutToPickupTheProduct() {
        val id = entities.first().id
        val contentMap = mapOf("employee" to "fulano de tal")
        restMockMvc.perform(put("$RESOURCE/{id}/pickup", id)
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
        restMockMvc.perform(put("$RESOURCE/{id}/taken", id)
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
        restMockMvc.perform(put("$RESOURCE/{id}/packaging", id)
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
        val result = restMockMvc.perform(put("$RESOURCE/{id}/ready", id)
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
        val message = DummyProducerConnector.findAll(FreightEvent::class.java).first()
        val event = message.content.toObject<FreightEvent>()
        Assertions.assertEquals(freightStatus, event.status)
    }

    private fun checkEventSourcingCommands(pickupRequest: PickupRequest) {
        val message = DummyProducerConnector.findAll(FreightMovePackageCommand::class.java).first()
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