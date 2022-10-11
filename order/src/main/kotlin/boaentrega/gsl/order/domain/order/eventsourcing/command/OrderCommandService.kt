package boaentrega.gsl.order.domain.order.eventsourcing.command


import boaentrega.gsl.order.configuration.constants.EventSourcingBeansConstants
import boaentrega.gsl.support.eventsourcing.connectors.ProducerConnector
import boaentrega.gsl.support.eventsourcing.messages.CommandMessage
import boaentrega.gsl.support.extensions.ClassExtensions.logger
import boaentrega.gsl.support.extensions.ClassExtensions.toJsonString
import org.springframework.beans.factory.annotation.Qualifier
import org.springframework.stereotype.Service

@Service
class OrderCommandService(
        @Qualifier(EventSourcingBeansConstants.CONTEXT_COMMAND_PRODUCER)
        private val producerConnector: ProducerConnector) {

    private val logger = logger()

    fun send(data: Any) {
        val message = CommandMessage(data)
        producerConnector.publish(message)
        logger.info("Command has been sent: ${message.toJsonString()}")
    }

}