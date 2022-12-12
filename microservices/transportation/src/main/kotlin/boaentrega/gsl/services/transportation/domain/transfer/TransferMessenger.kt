package boaentrega.gsl.services.transportation.domain.transfer

import boaentrega.gsl.configuration.constants.ServiceNames
import boaentrega.gsl.services.transportation.domain.transfer.eventsourcing.command.TransferFreightCommandService
import boaentrega.gsl.services.transportation.domain.transfer.eventsourcing.event.TransferFreightEventService
import org.springframework.stereotype.Component

@Component
class TransferMessenger(private val eventService: TransferFreightEventService,
                        private val commandService: TransferFreightCommandService) {

    fun create(entity: Transfer) {
        eventService.notifyPackageMoveStarted(entity.trackId, entity.freightId, ServiceNames.TRANSPORT,
                entity.currentPosition, "Package transfer process has been started")
    }

    fun movingToNextStorage(entity: Transfer) {
        eventService.notifyPackageMovingOnToNextStorage(entity.trackId, entity.freightId, ServiceNames.TRANSPORT,
                entity.currentPosition, "The package is being transferred to ${entity.nextStorage}")
    }

    fun receiveOnStorage(entity: Transfer) {
        eventService.notifyPackageReceivedByStorage(entity.trackId, entity.freightId, ServiceNames.TRANSPORT,
                entity.currentPosition, "The package was received by the storage: ${entity.nextStorage}")
    }

    fun finishMovement(entity: Transfer) {
        eventService.notifyPackageReachedFinalStorage(entity.trackId, entity.freightId, ServiceNames.TRANSPORT,
                entity.currentPosition, "The package arrived in the delivery area: ${entity.nextStorage}")
        commandService.deliverPackage(entity.trackId, entity.orderId, entity.freightId,
                entity.currentPosition, entity.deliveryAddress)
    }
}
