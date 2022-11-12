package boaentrega.gsl.order.domain.order.eventsourcing.command


import boaentrega.gsl.order.configuration.constants.EventSourcingBeanQualifiers
import boaentrega.gsl.order.support.eventsourcing.connectors.DedicatedProducerConnector
import boaentrega.gsl.order.support.eventsourcing.messages.CommandMessage
import boaentrega.gsl.order.support.extensions.ClassExtensions.logger
import boaentrega.gsl.order.support.extensions.ClassExtensions.toJsonString
import gsl.schemas.OrderApproveCommand
import gsl.schemas.OrderRefuseCommand
import org.springframework.beans.factory.annotation.Qualifier
import org.springframework.stereotype.Service
import java.math.BigDecimal
import java.time.Instant
import java.util.*

@Service
class OrderCommandService(
        @Qualifier(EventSourcingBeanQualifiers.ORDER_COMMAND_PRODUCER)
        private val dedicatedProducerConnector: DedicatedProducerConnector) {

    private val logger = logger()

    fun approve(trackId: UUID, orderId: UUID, value: BigDecimal) {
        val command = OrderApproveCommand(trackId, orderId, value, Instant.now())
        send(trackId, command)
    }

    fun refuse(trackId: UUID, orderId: UUID, reason: String) {
        val command = OrderRefuseCommand(trackId, orderId, reason, Instant.now())
        send(trackId, command)
    }

    private fun send(trackId: UUID?, command: Any) {
        val message = CommandMessage(trackId, command)
        dedicatedProducerConnector.publish(message)
        logger.info("Command has been send to [${dedicatedProducerConnector.getId()}]: ${message.toJsonString()}")
    }

}