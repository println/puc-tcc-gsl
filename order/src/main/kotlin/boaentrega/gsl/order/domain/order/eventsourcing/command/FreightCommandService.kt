package boaentrega.gsl.order.domain.order.eventsourcing.command


import boaentrega.gsl.order.configuration.constants.EventSourcingBeanQualifiers
import boaentrega.gsl.order.support.eventsourcing.connectors.DedicatedProducerConnector
import boaentrega.gsl.order.support.eventsourcing.messages.CommandMessage
import boaentrega.gsl.order.support.extensions.ClassExtensions.logger
import boaentrega.gsl.order.support.extensions.ClassExtensions.toJsonString
import gsl.schemas.*
import org.springframework.beans.factory.annotation.Qualifier
import org.springframework.stereotype.Service
import java.time.Instant
import java.util.*

@Service
class FreightCommandService(
        @Qualifier(EventSourcingBeanQualifiers.FREIGHT_COMMAND_PRODUCER)
        private val dedicatedProducerConnector: DedicatedProducerConnector) {

    private val logger = logger()

    fun create(trackId: UUID, orderId: UUID, senderAddress: String, deliveryAddress: String) {
        val command = FreightCreateCommand(trackId, orderId, senderAddress, deliveryAddress, Instant.now())
        send(trackId, command)
    }

    private fun send(trackId: UUID, command: Any) {
        val message = CommandMessage(trackId, command)
        dedicatedProducerConnector.publish(message)
        logger.info("Command has been send to [${dedicatedProducerConnector.getId()}]: ${message.toJsonString()}")
    }

}