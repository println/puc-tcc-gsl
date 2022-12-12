package boaentrega.gsl.services.collection.domain.eventsourcing

import boaentrega.gsl.services.collection.AbstractEventSourcingTest
import boaentrega.gsl.configuration.constants.EventSourcingBeanQualifiers
import boaentrega.gsl.configuration.constants.ResourcePaths
import boaentrega.gsl.services.collection.domain.pickup.PickupRequest
import boaentrega.gsl.services.collection.domain.pickup.PickupRequestRepository
import boaentrega.gsl.services.collection.domain.pickup.PickupRequestStatus
import boaentrega.gsl.services.collection.domain.pickup.web.PickupRequestController
import boaentrega.gsl.services.collection.domain.pickup.web.PickupRequestWebService
import boaentrega.gsl.support.eventsourcing.connectors.dummy.DummyConsumerConnector
import boaentrega.gsl.support.eventsourcing.connectors.dummy.DummyProducerConnector
import boaentrega.gsl.support.eventsourcing.messages.CommandMessage
import gsl.schemas.FreightEvent
import gsl.schemas.FreightEventStatus
import gsl.schemas.FreightPickupProductCommand
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.factory.annotation.Qualifier
import java.time.Instant
import java.util.*


class CollectionCommandTest : AbstractEventSourcingTest() {

    companion object {
        const val RESOURCE = ResourcePaths.COLLECTION
    }

    @Autowired
    private lateinit var repository: PickupRequestRepository

    @Autowired
    private lateinit var service: PickupRequestWebService

    @Autowired
    @Qualifier(EventSourcingBeanQualifiers.FREIGHT_COMMAND_CONSUMER)
    private lateinit var consumerConnector: DummyConsumerConnector

    override fun createResource(): Any {
        return PickupRequestController(service)
    }

    @AfterEach
    fun reset() {
        repository.deleteAll()
    }

    @Test
    fun command() {
        val command = createCommand()
        val message = CommandMessage(command.trackId, command)

        consumerConnector.consume(message)

        val pickupRequest = checkAllFromApiAndGetFirst<PickupRequest>(RESOURCE)

        Assertions.assertEquals(command.trackId, pickupRequest.trackId)
        Assertions.assertEquals(command.orderId, pickupRequest.orderId)
        Assertions.assertEquals(command.freightId, pickupRequest.freightId)
        Assertions.assertEquals(command.pickupAddress, pickupRequest.senderAddress)
        Assertions.assertEquals(command.deliveryAddress, pickupRequest.deliveryAddress)
        Assertions.assertEquals(PickupRequestStatus.WAITING, pickupRequest.status)
        Assertions.assertNull(pickupRequest.collectorEmployee)
        Assertions.assertNull(pickupRequest.packerEmployee)
        Assertions.assertNull(pickupRequest.currentPosition)

        assertTotalMessagesAndReleaseThem()
        val eventContent = DummyProducerConnector.getMessageContent(FreightEvent::class)
        Assertions.assertEquals(FreightEventStatus.COLLECTION_STARTED, eventContent?.status)
    }

    @Test
    fun commandDuplication() {
        val command = createCommand()
        val message = CommandMessage(command.trackId, command)
        repeat(3) {
            consumerConnector.consume(message)
        }
        checkAllFromApiAndGetFirst<PickupRequest>(RESOURCE)
        assertTotalMessagesAndReleaseThem(1)
    }

    private fun createCommand(): FreightPickupProductCommand {
        val trackId = UUID.randomUUID()
        val orderId = UUID.randomUUID()
        val freightId = UUID.randomUUID()
        val pickupAddress = "pickupAddress"
        val deliveryAddress = "deliveryAddress"
        val date = Instant.now()
        return FreightPickupProductCommand(trackId, orderId, freightId, pickupAddress, deliveryAddress, date)
    }


}