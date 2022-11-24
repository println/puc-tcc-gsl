package boaentrega.gsl.collection.domain.pickup.eventsourcing.command

import boaentrega.gsl.collection.configuration.constants.EventSourcingBeanQualifiers
import boaentrega.gsl.collection.domain.pickup.PickupRequestService
import boaentrega.gsl.collection.support.eventsourcing.connectors.ConsumerConnector
import boaentrega.gsl.collection.support.eventsourcing.controller.AbstractConsumerController
import boaentrega.gsl.collection.support.eventsourcing.controller.annotations.ConsumptionHandler
import gsl.schemas.*
import org.springframework.beans.factory.annotation.Qualifier
import org.springframework.stereotype.Component

@Component
class FreightCommandController(
        @Qualifier(EventSourcingBeanQualifiers.FREIGHT_COMMAND_CONSUMER)
        consumerConnector: ConsumerConnector,
        private val pickupRequestService: PickupRequestService) : AbstractConsumerController(consumerConnector) {

    @ConsumptionHandler(FreightPickupProductCommand::class)
    fun pickupProduct(command: FreightPickupProductCommand) {
        pickupRequestService.createPickupRequest(command.trackId, command.orderId, command.freightId,
                command.pickupAddress, command.deliveryAddress)
    }
}