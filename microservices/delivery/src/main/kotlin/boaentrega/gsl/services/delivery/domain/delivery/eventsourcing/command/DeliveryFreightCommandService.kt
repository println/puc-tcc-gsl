package boaentrega.gsl.services.delivery.domain.delivery.eventsourcing.command


import boaentrega.gsl.configuration.constants.EventSourcingBeanQualifiers
import boaentrega.gsl.support.eventsourcing.connectors.DedicatedProducerConnector
import boaentrega.gsl.support.eventsourcing.messages.CommandMessage
import boaentrega.gsl.support.extensions.ClassExtensions.logger
import boaentrega.gsl.support.extensions.ClassExtensions.toJsonString
import gsl.schemas.*
import org.springframework.beans.factory.annotation.Qualifier
import org.springframework.stereotype.Service
import java.time.Instant
import java.util.*

@Service
class DeliveryFreightCommandService(
        @Qualifier(EventSourcingBeanQualifiers.FREIGHT_COMMAND_PRODUCER)
        private val dedicatedProducerConnector: DedicatedProducerConnector) {

    private val logger = logger()

    fun finish(trackId: UUID, orderId: UUID, freightId: UUID) {
        val command = FreightFinishCommand(trackId, orderId, freightId, Instant.now())
        send(trackId, command)
    }

    private fun send(trackId: UUID, command: Any) {
        val message = CommandMessage(trackId, command)
        dedicatedProducerConnector.publish(message)
        logger.info("Command has been send to [${dedicatedProducerConnector.getId()}]: ${message.toJsonString()}")
    }

}