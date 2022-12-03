package boaentrega.gsl.order.domain.eventsourcing

import boaentrega.gsl.order.AbstractEventSourcingTest
import boaentrega.gsl.order.configuration.constants.EventSourcingBeanQualifiers
import boaentrega.gsl.order.configuration.constants.ResourcePaths
import boaentrega.gsl.order.domain.delivery.Delivery
import boaentrega.gsl.order.domain.delivery.DeliveryRepository
import boaentrega.gsl.order.domain.delivery.DeliveryStatus
import boaentrega.gsl.order.domain.delivery.web.DeliveryController
import boaentrega.gsl.order.domain.delivery.web.DeliveryWebService
import boaentrega.gsl.support.eventsourcing.connectors.dummy.DummyConsumerConnector
import boaentrega.gsl.support.eventsourcing.connectors.dummy.DummyProducerConnector
import boaentrega.gsl.support.eventsourcing.messages.CommandMessage
import gsl.schemas.FreightDeliverPackageCommand
import gsl.schemas.FreightEvent
import gsl.schemas.FreightEventStatus
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.factory.annotation.Qualifier
import java.time.Instant
import java.util.*


class DeliveryCommandTest : AbstractEventSourcingTest() {

    companion object {
        const val RESOURCE = ResourcePaths.DELIVERY
    }

    @Autowired
    private lateinit var repository: DeliveryRepository

    @Autowired
    private lateinit var service: DeliveryWebService

    @Autowired
    @Qualifier(EventSourcingBeanQualifiers.FREIGHT_COMMAND_CONSUMER)
    private lateinit var consumerConnector: DummyConsumerConnector

    override fun createResource(): Any {
        return DeliveryController(service)
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

        val delivery = checkAllFromApiAndGetFirst<Delivery>(RESOURCE)

        Assertions.assertEquals(command.trackId, delivery.trackId)
        Assertions.assertEquals(command.orderId, delivery.orderId)
        Assertions.assertEquals(command.freightId, delivery.freightId)
        Assertions.assertEquals(command.currentPosition, delivery.currentPosition)
        Assertions.assertEquals(command.deliveryAddress, delivery.deliveryAddress)
        Assertions.assertEquals(DeliveryStatus.CREATED, delivery.status)
        Assertions.assertNull(delivery.preferredDeliveryTime)

        assertTotalMessagesAndReleaseThem()
        val eventContent = DummyProducerConnector.getMessageContent(FreightEvent::class)
        Assertions.assertEquals(FreightEventStatus.DELIVERY_STARTED, eventContent?.status)
    }

    @Test
    fun commandDuplication() {
        val command = createCommand()
        val message = CommandMessage(command.trackId, command)
        repeat(3) {
            consumerConnector.consume(message)
        }
        checkAllFromApiAndGetFirst<Delivery>(RESOURCE)
        assertTotalMessagesAndReleaseThem(1)
    }

    private fun createCommand(): FreightDeliverPackageCommand {
        val trackId = UUID.randomUUID()
        val orderId = UUID.randomUUID()
        val freightId = UUID.randomUUID()
        val currentPosition = "currentPosition"
        val deliveryAddress = "deliveryAddress"
        val date = Instant.now()
        return FreightDeliverPackageCommand(trackId, orderId, freightId, currentPosition, deliveryAddress, date)
    }


}