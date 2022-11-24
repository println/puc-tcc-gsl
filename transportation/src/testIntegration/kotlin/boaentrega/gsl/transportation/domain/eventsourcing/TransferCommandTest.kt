package boaentrega.gsl.transportation.domain.eventsourcing

import boaentrega.gsl.transportation.AbstractEventSourcingTest
import boaentrega.gsl.transportation.configuration.constants.EventSourcingBeanQualifiers
import boaentrega.gsl.transportation.configuration.constants.ResourcePaths
import boaentrega.gsl.transportation.domain.transfer.Transfer
import boaentrega.gsl.transportation.domain.transfer.TransferRepository
import boaentrega.gsl.transportation.domain.transfer.TransferStatus
import boaentrega.gsl.transportation.domain.transfer.web.TransferController
import boaentrega.gsl.transportation.domain.transfer.web.TransferWebService
import boaentrega.gsl.transportation.support.eventsourcing.connectors.dummy.DummyConsumerConnector
import boaentrega.gsl.transportation.support.eventsourcing.connectors.dummy.DummyProducerConnector
import boaentrega.gsl.transportation.support.eventsourcing.messages.CommandMessage
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


class TransferCommandTest : AbstractEventSourcingTest() {

    companion object {
        const val RESOURCE = ResourcePaths.TRANSPORT
    }

    @Autowired
    private lateinit var repository: TransferRepository

    @Autowired
    private lateinit var service: TransferWebService

    @Autowired
    @Qualifier(EventSourcingBeanQualifiers.FREIGHT_COMMAND_CONSUMER)
    private lateinit var consumerConnector: DummyConsumerConnector

    override fun createResource(): Any {
        return TransferController(service)
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

        val transfer = checkAllFromApiAndGetFirst<Transfer>(RESOURCE)

        Assertions.assertEquals(command.trackId, transfer.trackId)
        Assertions.assertEquals(command.orderId, transfer.orderId)
        Assertions.assertEquals(command.freightId, transfer.freightId)
        Assertions.assertEquals(command.currentPosition, transfer.currentPosition)
        Assertions.assertEquals(command.deliveryAddress, transfer.deliveryAddress)
        Assertions.assertEquals(TransferStatus.CREATED, transfer.status)
        Assertions.assertNotNull(transfer.nextStorage)
        Assertions.assertNotNull(transfer.finalStorage)

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
        checkAllFromApiAndGetFirst<Transfer>(RESOURCE)
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