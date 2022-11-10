package boaentrega.gsl.order.domain.order.eventsourcing.event

import boaentrega.gsl.order.configuration.constants.EventSourcingBeanQualifiers
import boaentrega.gsl.order.domain.order.OrderService
import boaentrega.gsl.order.support.eventsourcing.connectors.ConsumerConnector
import boaentrega.gsl.order.support.eventsourcing.controller.AbstractConsumerController
import boaentrega.gsl.order.support.eventsourcing.controller.annotations.ConsumptionHandler
import gsl.schemas.FreightEvent
import gsl.schemas.FreightEventStatus.*
import org.springframework.beans.factory.annotation.Qualifier
import org.springframework.stereotype.Component

@Component
class FreightEventController(
        @Qualifier(EventSourcingBeanQualifiers.FREIGHT_EVENT_CONSUMER)
        consumerConnector: ConsumerConnector,
        private val service: OrderService
) : AbstractConsumerController(consumerConnector) {

    @ConsumptionHandler(FreightEvent::class)
    fun listen(event: FreightEvent) {
        when (event.status) {
            CREATED -> {}
            FINISHED -> {}
            CANCELING -> {}
            CANCELED -> {}
            COLLECTION_STARTED -> {}
            COLLECTION_PICKUP_OUT -> {}
            COLLECTION_PICKUP_TAKEN -> {}
            COLLECTION_PACKAGE_PREPARING -> {}
            COLLECTION_PACKAGE_READY_TO_MOVE -> {}
            PACKAGE_MOVE_STARTED -> {}
            PACKAGE_MOVING_ON_TO_NEXT_STORAGE -> {}
            PACKAGE_RECEIVED_BY_STORAGE -> {}
            PACKAGE_REACHED_FINAL_STORAGE -> {}
            PACKAGE_OUT_FOR_DELIVERY -> {}
            PACKAGE_DELIVERED -> {}
            PACKAGE_DELIVERY_FAILED -> {}
            PACKAGE_DELIVERY_PROCESS_RESET -> {}
            else -> {}
        }
    }
}