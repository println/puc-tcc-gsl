package boaentrega.gsl.order.domain.order.eventsourcing.event

import boaentrega.gsl.order.configuration.constants.EventSourcingBeansConstants
import boaentrega.gsl.order.support.eventsourcing.connectors.DedicatedProducerConnector
import boaentrega.gsl.order.support.eventsourcing.messages.EventMessage
import boaentrega.gsl.order.support.extensions.ClassExtensions.logger
import boaentrega.gsl.order.support.extensions.ClassExtensions.toJsonString
import gsl.schemas.OrderEvent
import gsl.schemas.OrderEventStatus
import org.springframework.beans.factory.annotation.Qualifier
import org.springframework.stereotype.Service
import java.time.LocalDate
import java.util.*

@Service
class OrderEventService(
        @Qualifier(EventSourcingBeansConstants.ORDER_EVENT_PRODUCER)
        private val dedicatedProducerConnector: DedicatedProducerConnector) {

    private val logger = logger()

    companion object {
        const val ORDER_SERVICE = "order"
    }

    fun notifyOrderCreated(trackId: UUID) {
        val source = ORDER_SERVICE
        val description = "Order created successfully"
        val status = OrderEventStatus.CREATED
        notify(trackId, status, source, description)
    }

    fun notifyOrderProcessingPayment(trackId: UUID, source: String, description: String) {
        val source = ORDER_SERVICE
        val description = "Order is waiting payment process"
        val status = OrderEventStatus.PROCESSING_PAYMENT
        notify(trackId, status, source, description)
    }

    fun notifyOrderAccepted(trackId: UUID) {
        val source = ORDER_SERVICE
        val description = "Order accepted"
        val status = OrderEventStatus.ACCEPTED
        notify(trackId, status, source, description)
    }

    fun notifyOrderRefused(trackId: UUID) {
        val source = ORDER_SERVICE
        val description = "Order refused"
        val status = OrderEventStatus.REFUSED
        notify(trackId, status, source, description)
    }

    private fun notify(trackId: UUID, status: OrderEventStatus, source: String, description: String) {
        val payload = OrderEvent(trackId, status, source, description, LocalDate.now())
        val message = EventMessage(trackId, payload)
        dedicatedProducerConnector.publish(message)
        logger.info("Event has been emitted to [${dedicatedProducerConnector.getId()}]: ${message.toJsonString()}")
    }
}