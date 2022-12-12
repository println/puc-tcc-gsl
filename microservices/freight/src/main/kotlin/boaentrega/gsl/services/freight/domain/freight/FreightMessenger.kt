package boaentrega.gsl.services.freight.domain.freight

import boaentrega.gsl.configuration.constants.ServiceNames
import boaentrega.gsl.services.freight.configuration.constants.Qualifiers
import boaentrega.gsl.services.freight.domain.freight.eventsourcing.command.FreightCommandService
import boaentrega.gsl.services.freight.domain.freight.eventsourcing.document.FreightDocumentBroadcastService
import boaentrega.gsl.services.freight.domain.freight.eventsourcing.event.FreightEventService
import org.springframework.beans.factory.annotation.Qualifier
import org.springframework.stereotype.Component

@Component
class FreightMessenger(
        @Qualifier(Qualifiers.APP_EVENT_SERVICE)
        private val freightEventService: FreightEventService,
        @Qualifier(Qualifiers.APP_COMMAND_SERVICE)
        private val freightCommandService: FreightCommandService,
        @Qualifier(Qualifiers.APP_DOCUMENT_SERVICE)
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