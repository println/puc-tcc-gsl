package boaentrega.gsl.order.eventsourcing

import boaentrega.gsl.order.AbstractIntegrationTest
import boaentrega.gsl.order.configuration.constants.EventSourcingBeansConstants
import boaentrega.gsl.order.domain.collector.PickupRequest
import boaentrega.gsl.order.domain.collector.PickupRequestRepository
import boaentrega.gsl.order.domain.collector.PickupRequestService
import boaentrega.gsl.order.domain.collector.PickupRequestStatus
import boaentrega.gsl.order.domain.collector.web.CollectorController
import boaentrega.gsl.order.support.eventsourcing.connectors.dummy.DummyConsumerConnector
import boaentrega.gsl.order.support.eventsourcing.connectors.dummy.DummyProducerConnector
import boaentrega.gsl.order.support.eventsourcing.messages.CommandMessage
import boaentrega.gsl.order.support.extensions.ClassExtensions.toObject
import boaentrega.gsl.order.support.outbox.OutboxConnectorService
import boaentrega.gsl.order.support.outbox.OutboxRepository
import boaentrega.gsl.order.support.web.ResponsePage
import gsl.schemas.FreightEvent
import gsl.schemas.FreightEventStatus
import gsl.schemas.FreightPickupProductCommand
import org.hamcrest.Matchers
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.factory.annotation.Qualifier
import org.springframework.http.MediaType
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders
import org.springframework.test.web.servlet.result.MockMvcResultMatchers
import java.time.LocalDate
import java.util.*


class CollectorCommandTest : AbstractIntegrationTest() {
    companion object{
        const val RESOURCE = "/collector"
    }

    @Autowired
    private lateinit var repository: PickupRequestRepository

    @Autowired
    private lateinit var service: PickupRequestService

    @Autowired
    private lateinit var outboxRepository: OutboxRepository

    @Autowired
    private lateinit var outboxConnectorService: OutboxConnectorService

    @Autowired
    @Qualifier(EventSourcingBeansConstants.FREIGHT_COMMAND_CONSUMER)
    private lateinit var consumerConnector: DummyConsumerConnector

    override fun createResource(): Any {
        return CollectorController(service)
    }

    @AfterEach
    fun reset() {
        repository.deleteAll()
        outboxRepository.deleteAll()
        DummyProducerConnector.clearMessages()
    }

    @Test
    fun command() {
        val command = createCommand()
        val message = CommandMessage(command.trackId, command)

        consumerConnector.consume(message)

        val pickupRequest = findAll()

        Assertions.assertEquals(command.trackId, pickupRequest.trackId)
        Assertions.assertEquals(command.orderId, pickupRequest.orderId)
        Assertions.assertEquals(command.freightId, pickupRequest.freightId)
        Assertions.assertEquals(command.pickupAddress, pickupRequest.pickupAddress)
        Assertions.assertEquals(command.destination, pickupRequest.destination)
        Assertions.assertEquals(PickupRequestStatus.WAITING, pickupRequest.status)
        Assertions.assertNull(pickupRequest.collectorEmployee)
        Assertions.assertNull(pickupRequest.packerEmployee)
        Assertions.assertNull(pickupRequest.packageAddress)

        releaseMessages()
        checkEventSourcingEvents(FreightEventStatus.COLLECTION_STARTED)
    }

    @Test
    fun commandDuplication() {
        val command = createCommand()
        val message = CommandMessage(command.trackId, command)

        consumerConnector.consume(message)
        consumerConnector.consume(message)

        findAll()

        Assertions.assertEquals(1 ,outboxRepository.getTop10ByIsPublishedFalse().size)
        releaseMessages()
    }

    private fun createCommand(): FreightPickupProductCommand {
        val trackId = UUID.randomUUID()
        val orderId = UUID.randomUUID()
        val freightId = UUID.randomUUID()
        val pickupAddress = "pickupAddress"
        val destination = "destination"
        val date = LocalDate.now()
        return FreightPickupProductCommand(trackId, orderId, freightId, pickupAddress, destination, date)
    }

    private fun findAll(): PickupRequest {
        val result = restMockMvc.perform(MockMvcRequestBuilders.get(RESOURCE)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk)
                .andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(MockMvcResultMatchers.jsonPath("\$.content", Matchers.hasSize<String>(1)))
                .andReturn()

        val response = result.response
        val page = response.contentAsString.toObject<ResponsePage>()
        return page.getObject<PickupRequest>()[0]
    }

    private fun checkEventSourcingEvents(freightStatus: FreightEventStatus) {
        val message = DummyProducerConnector.findAll(FreightEvent::class.java).first()
        val event = message.content.toObject<FreightEvent>()
        Assertions.assertEquals(freightStatus, event.status)
    }

    private fun releaseMessages() {
        Assertions.assertTrue(outboxRepository.getTop10ByIsPublishedFalse().isNotEmpty())
        outboxConnectorService.releaseMessages()
        Assertions.assertTrue(outboxRepository.getTop10ByIsPublishedFalse().isEmpty())
    }
}