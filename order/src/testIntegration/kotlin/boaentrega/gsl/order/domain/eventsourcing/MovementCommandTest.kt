package boaentrega.gsl.order.domain.eventsourcing

import boaentrega.gsl.order.AbstractEventSourcingTest
import boaentrega.gsl.order.configuration.constants.EventSourcingBeanQualifiers
import boaentrega.gsl.order.configuration.constants.ResourcePaths
import boaentrega.gsl.order.domain.transportation.Movement
import boaentrega.gsl.order.domain.transportation.MovementRepository
import boaentrega.gsl.order.domain.transportation.MovementService
import boaentrega.gsl.order.domain.transportation.MovementStatus
import boaentrega.gsl.order.domain.transportation.web.MovementController
import boaentrega.gsl.order.support.eventsourcing.connectors.dummy.DummyConsumerConnector
import boaentrega.gsl.order.support.eventsourcing.connectors.dummy.DummyProducerConnector
import boaentrega.gsl.order.support.eventsourcing.messages.CommandMessage
import gsl.schemas.FreightEvent
import gsl.schemas.FreightEventStatus
import gsl.schemas.FreightMovePackageCommand
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.factory.annotation.Qualifier
import java.time.Instant
import java.util.*


class MovementCommandTest : AbstractEventSourcingTest() {

    companion object {
        const val RESOURCE = ResourcePaths.TRANSPORT
    }

    @Autowired
    private lateinit var repository: MovementRepository

    @Autowired
    private lateinit var service: MovementService

    @Autowired
    @Qualifier(EventSourcingBeanQualifiers.FREIGHT_COMMAND_CONSUMER)
    private lateinit var consumerConnector: DummyConsumerConnector

    override fun createResource(): Any {
        return MovementController(service)
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

        val movement = checkAllFromApiAndGetFirst<Movement>(RESOURCE)

        Assertions.assertEquals(command.trackId, movement.trackId)
        Assertions.assertEquals(command.orderId, movement.orderId)
        Assertions.assertEquals(command.freightId, movement.freightId)
        Assertions.assertEquals(command.currentPosition, movement.currentPosition)
        Assertions.assertEquals(command.deliveryAddress, movement.deliveryAddress)
        Assertions.assertEquals(MovementStatus.CREATED, movement.status)
        Assertions.assertNotNull(movement.nextStorage)
        Assertions.assertNotNull(movement.finalStorage)

        assertTotalMessagesAndReleaseThem()
        val eventContent = DummyProducerConnector.getMessageContent(FreightEvent::class)
        Assertions.assertEquals(FreightEventStatus.IN_TRANSIT_PACKAGE_STARTED, eventContent?.status)
    }

    @Test
    fun commandDuplication() {
        val command = createCommand()
        val message = CommandMessage(command.trackId, command)
        repeat(3) {
            consumerConnector.consume(message)
        }
        checkAllFromApiAndGetFirst<Movement>(RESOURCE)
        assertTotalMessagesAndReleaseThem(1)
    }

    private fun createCommand(): FreightMovePackageCommand {
        val trackId = UUID.randomUUID()
        val orderId = UUID.randomUUID()
        val freightId = UUID.randomUUID()
        val currentPosition = "currentPosition"
        val deliveryAddress = "deliveryAddress"
        val date = Instant.now()
        return FreightMovePackageCommand(trackId, orderId, freightId, currentPosition, deliveryAddress, date)
    }


}