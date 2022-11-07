package boaentrega.gsl.order.domain.order.eventsourcing.event

import boaentrega.gsl.order.configuration.constants.EventSourcingBeansConstants
import boaentrega.gsl.order.domain.order.OrderService
import boaentrega.gsl.order.support.eventsourcing.connectors.ConsumerConnector
import boaentrega.gsl.order.support.eventsourcing.controller.AbstractConsumerController
import boaentrega.gsl.order.support.eventsourcing.controller.annotations.ConsumptionHandler
import gsl.schemas.OrderEvent
import gsl.schemas.OrderEventStatus.*
import org.springframework.beans.factory.annotation.Qualifier
import org.springframework.stereotype.Component

@Component
class OrderEventController(
        @Qualifier(EventSourcingBeansConstants.ORDER_EVENT_CONSUMER)
        consumerConnector: ConsumerConnector,
        private val service: OrderService
) : AbstractConsumerController(consumerConnector) {

    @ConsumptionHandler(OrderEvent::class)
    fun listen(event: OrderEvent) {
        when (event.status) {
            CREATED -> {}
            PROCESSING_PAYMENT -> {}
            REFUSED -> {}
            ACCEPTED -> {}
            else -> {}
        }
    }
}