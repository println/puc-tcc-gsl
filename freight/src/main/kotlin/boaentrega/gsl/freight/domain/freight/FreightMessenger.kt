package boaentrega.gsl.freight.domain.freight

import boaentrega.gsl.freight.configuration.constants.ServiceNames
import boaentrega.gsl.freight.domain.freight.eventsourcing.command.FreightCommandService
import boaentrega.gsl.freight.domain.freight.eventsourcing.document.FreightDocumentBroadcastService
import boaentrega.gsl.freight.domain.freight.eventsourcing.event.FreightEventService
import org.springframework.stereotype.Component

@Component
class FreightMessenger(private val freightEventService: FreightEventService,
                       private val freightCommandService: FreightCommandService,
                       private val freightDocumentBroadcastService: FreightDocumentBroadcastService) {
    fun createFreight(entity: Freight) {
        freightEventService.notifyCreated(entity.trackId, entity.id!!, ServiceNames.FREIGHT, entity.currentPosition,
                "Freight process has been created", entity.lastUpdated)
        freightCommandService.pickupProduct(entity.trackId, entity.orderId, entity.id!!, entity.senderAddress,
                entity.deliveryAddress)
        freightDocumentBroadcastService.release(entity)
    }

    fun updateStatus(entity: Freight) {
        freightDocumentBroadcastService.release(entity)
    }

    fun finishFreight(entity: Freight) {
        freightEventService.notifyFinished(entity.trackId, entity.id!!, ServiceNames.FREIGHT,
                entity.currentPosition, "Freight process has been updated", entity.lastUpdated)
        freightDocumentBroadcastService.release(entity)
    }
}