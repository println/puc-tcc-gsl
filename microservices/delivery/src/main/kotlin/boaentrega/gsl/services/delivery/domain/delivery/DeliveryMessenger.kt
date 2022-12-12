package boaentrega.gsl.services.delivery.domain.delivery

import boaentrega.gsl.configuration.constants.ServiceNames
import boaentrega.gsl.services.delivery.domain.delivery.eventsourcing.command.DeliveryFreightCommandService
import boaentrega.gsl.services.delivery.domain.delivery.eventsourcing.event.DeliveryFreightEventService
import org.springframework.stereotype.Component

@Component
class DeliveryMessenger(
        private val eventService: DeliveryFreightEventService,
        private val commandService: DeliveryFreightCommandService) {

    fun create(entity: Delivery) {
        eventService.notifyPackageDeliveryStarted(entity.trackId, entity.freightId, ServiceNames.DELIVERY,
                entity.currentPosition, "Delivery process started")
    }

    fun takePackageToDelivery(entity: Delivery) {
        eventService.notifyPackageOutForDelivery(entity.trackId, entity.freightId, ServiceNames.DELIVERY,
                entity.currentPosition, "Out for delivery")
    }

    fun markAsDeliveryFailed(entity: Delivery) {
        eventService.notifyPackageDeliveryFailed(entity.trackId, entity.freightId, ServiceNames.DELIVERY,
                entity.currentPosition, "Unable to deliver")
    }

    fun giveBackPackageToRetryDelivery(entity: Delivery) {
        eventService.notifyPackageDeliveryProcessReset(entity.trackId, entity.freightId, ServiceNames.DELIVERY,
                entity.currentPosition, "We will make a new delivery attempt on another day")
    }

    fun markAsSuccessfulDelivery(entity: Delivery) {
        eventService.notifyPackageDelivered(entity.trackId, entity.freightId, ServiceNames.DELIVERY,
                entity.currentPosition, "Delivery process has been successfully")
        commandService.finish(entity.trackId, entity.orderId, entity.freightId)
    }
}