package boaentrega.gsl.services.collection.domain.pickup

import boaentrega.gsl.configuration.constants.ServiceNames
import boaentrega.gsl.services.collection.domain.pickup.eventsourcing.command.CollectionFreightCommandService
import boaentrega.gsl.services.collection.domain.pickup.eventsourcing.event.CollectionFreightEventService
import org.springframework.stereotype.Component

@Component
class PickupRequestMessenger(private val eventService: CollectionFreightEventService,
                             private val commandService: CollectionFreightCommandService) {
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