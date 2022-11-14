package boaentrega.gsl.order.domain.transportation

import boaentrega.gsl.order.configuration.constants.ServiceNames
import boaentrega.gsl.order.domain.order.eventsourcing.command.FreightCommandService
import boaentrega.gsl.order.domain.order.eventsourcing.event.FreightEventService
import org.springframework.stereotype.Component

@Component
class MovementMessenger(private val eventService: FreightEventService,
                        private val commandService: FreightCommandService) {

    fun create(entity: Movement) {
        eventService.notifyPackageMoveStarted(entity.trackId, entity.freightId, ServiceNames.TRANSPORT,
                entity.currentPosition, "Package transfer process has been started")
    }

    fun movingToNextStorage(entity: Movement) {
        eventService.notifyPackageMovingOnToNextStorage(entity.trackId, entity.freightId, ServiceNames.TRANSPORT,
                entity.currentPosition, "The package is being transferred to ${entity.nextStorage}")
    }

    fun receiveOnStorage(entity: Movement) {
        eventService.notifyPackageReceivedByStorage(entity.trackId, entity.freightId, ServiceNames.TRANSPORT,
                entity.currentPosition, "The package was received by the storage: ${entity.nextStorage}")
    }

    fun finishMovement(entity: Movement) {
        eventService.notifyPackageReachedFinalStorage(entity.trackId, entity.freightId, ServiceNames.TRANSPORT,
                entity.currentPosition, "The package arrived in the delivery area: ${entity.nextStorage}")
        commandService.deliverPackage(entity.trackId, entity.orderId, entity.freightId,
                entity.currentPosition, entity.deliveryAddress)
    }
}
