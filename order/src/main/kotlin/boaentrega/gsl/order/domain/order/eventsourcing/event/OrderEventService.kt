package boaentrega.gsl.order.domain.order.eventsourcing.event

import boaentrega.gsl.order.configuration.constants.EventSourcingBeansConstants
import boaentrega.gsl.support.eventsourcing.connectors.ProducerConnector
import boaentrega.gsl.support.eventsourcing.messages.EventMessage
import boaentrega.gsl.support.extensions.ClassExtensions.logger
import boaentrega.gsl.support.extensions.ClassExtensions.toJsonString
import org.springframework.beans.factory.annotation.Qualifier
import org.springframework.stereotype.Service

@Service
class OrderEventService(
        @Qualifier(EventSourcingBeansConstants.CONTEXT_EVENT_PRODUCER)
        private val producerConnector: ProducerConnector) {

    private val logger = logger()

    fun emit(data: Any) {
        val message = EventMessage(data)
        producerConnector.publish(message)
        logger.info("Event has been emitted: ${message.toJsonString()}")
    }
}