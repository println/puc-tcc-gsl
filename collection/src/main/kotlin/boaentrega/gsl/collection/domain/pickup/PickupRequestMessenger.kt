package boaentrega.gsl.collection.domain.pickup

import boaentrega.gsl.collection.configuration.constants.ServiceNames
import boaentrega.gsl.collection.domain.pickup.eventsourcing.command.FreightCommandService
import boaentrega.gsl.collection.domain.pickup.eventsourcing.event.FreightEventService
import org.springframework.stereotype.Component

@Component
class PickupRequestMessenger(private val eventService: FreightEventService,
                             private val commandService: FreightCommandService) {
    fun createPickupRequest(entity: PickupRequest) {
        eventService.notifyCollectionStarted(entity.trackId, entity.freightId, ServiceNames.COLLECTION,
                entity.senderAddress, "Pickup process started")
    }

    fun markAsOutToPickupTheProduct(entity: PickupRequest) {
        eventService.notifyCollectionPickupOut(entity.trackId, entity.freightId, ServiceNames.COLLECTION,
                entity.senderAddress, "The employee went out to pick up the product from the customer")
    }

    fun markAsTaken(entity: PickupRequest) {
        eventService.notifyCollectionPickupTaken(entity.trackId, entity.freightId,
                ServiceNames.COLLECTION, entity.currentPosition!!, " The product is already with us ")
    }

    fun markAsOnPackaging(entity: PickupRequest) {
        eventService.notifyCollectionPackagePreparing(entity.trackId, entity.freightId,
                ServiceNames.COLLECTION, entity.currentPosition!!, "We are making the transfer package")
    }

    fun markAsReadyToStartDelivery(entity: PickupRequest) {
        eventService.notifyCollectionPackageReadyToMove(entity.trackId, entity.freightId,
                ServiceNames.COLLECTION, entity.currentPosition!!, "All ready to start shipping")
        commandService.movePackage(entity.trackId, entity.orderId, entity.freightId,
                entity.currentPosition!!, entity.deliveryAddress)
    }
}