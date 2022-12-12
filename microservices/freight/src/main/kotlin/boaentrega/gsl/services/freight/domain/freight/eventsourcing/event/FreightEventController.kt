package boaentrega.gsl.services.freight.domain.freight.eventsourcing.event

import boaentrega.gsl.configuration.constants.EventSourcingBeanQualifiers
import boaentrega.gsl.services.freight.configuration.constants.Qualifiers
import boaentrega.gsl.services.freight.domain.freight.FreightService
import boaentrega.gsl.services.freight.domain.freight.FreightStatus
import boaentrega.gsl.support.eventsourcing.connectors.ConsumerConnector
import boaentrega.gsl.support.eventsourcing.controller.AbstractConsumerController
import boaentrega.gsl.support.eventsourcing.controller.annotations.ConsumptionHandler
import boaentrega.gsl.schemas.FreightEvent
import boaentrega.gsl.schemas.FreightEventStatus.*
import org.springframework.beans.factory.annotation.Qualifier
import org.springframework.stereotype.Component

@Component
@Qualifier(Qualifiers.APP_EVENT_CONTROLLER)
class FreightEventController(
        @Qualifier(EventSourcingBeanQualifiers.FREIGHT_EVENT_CONSUMER)
        consumerConnector: ConsumerConnector,
        private val service: FreightService
) : AbstractConsumerController(consumerConnector) {

    @ConsumptionHandler(FreightEvent::class)
    fun listen(event: FreightEvent) {

        var status: FreightStatus = FreightStatus.valueOf(event.status.name)

        when (event.status) {
            CREATED,
            FINISHED,
            CANCELING,
            CANCELED -> {
            }
            COLLECTION_STARTED,
            COLLECTION_PICKUP_OUT,
            COLLECTION_PICKUP_TAKEN,
            COLLECTION_PACKAGE_PREPARING,
            COLLECTION_PACKAGE_READY_TO_MOVE,
            IN_TRANSIT_PACKAGE_STARTED,
            IN_TRANSIT_PACKAGE_MOVING_ON_TO_NEXT_STORAGE,
            IN_TRANSIT_PACKAGE_RECEIVED_BY_STORAGE,
            IN_TRANSIT_PACKAGE_REACHED_FINAL_STORAGE,
            DELIVERY_STARTED,
            DELIVERY_OUT_FOR,
            DELIVERY_FAILED,
            DELIVERY_PROCESS_RESTART -> {
                service.updateStatus(event.trackId, event.freightId, status, event.currentPosition, event.date)
            }
            DELIVERY_SUCCESS -> {
                service.deliverySuccessfully(event.freightId, event.date)
            }
        }
    }
}