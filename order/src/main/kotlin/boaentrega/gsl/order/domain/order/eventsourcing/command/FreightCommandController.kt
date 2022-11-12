package boaentrega.gsl.order.domain.order.eventsourcing.command

import boaentrega.gsl.order.configuration.constants.EventSourcingBeanQualifiers
import boaentrega.gsl.order.domain.collection.PickupRequestService
import boaentrega.gsl.order.domain.freight.FreightService
import boaentrega.gsl.order.support.eventsourcing.connectors.ConsumerConnector
import boaentrega.gsl.order.support.eventsourcing.controller.AbstractConsumerController
import boaentrega.gsl.order.support.eventsourcing.controller.annotations.ConsumptionHandler
import gsl.schemas.*
import org.springframework.beans.factory.annotation.Qualifier
import org.springframework.stereotype.Component

@Component
class FreightCommandController(
        @Qualifier(EventSourcingBeanQualifiers.FREIGHT_COMMAND_CONSUMER)
        consumerConnector: ConsumerConnector,
        private val pickupRequestService: PickupRequestService,
        private val freightService: FreightService
) : AbstractConsumerController(consumerConnector) {

    @ConsumptionHandler(FreightCreateCommand::class)
    fun create(command: FreightCreateCommand) {
        freightService.createFreight(command.trackId, command.orderId, command.pickupAddress, command.deliveryAddress)
    }

    @ConsumptionHandler(FreightPickupProductCommand::class)
    fun pickupProduct(command: FreightPickupProductCommand) {
        pickupRequestService.createPickupRequest(command.trackId, command.orderId, command.freightId, command.pickupAddress, command.destination)
    }

    @ConsumptionHandler(FreightMovePackageCommand::class)
    fun movePackage(command: FreightMovePackageCommand) {

    }

    @ConsumptionHandler(FreightDeliverPackageCommand::class)
    fun deliverPackage(command: FreightDeliverPackageCommand) {

    }

    @ConsumptionHandler(FreightFinishCommand::class)
    fun finish(command: FreightFinishCommand) {
        freightService.finishFreight(command.freightId)
    }

    @ConsumptionHandler(FreightCancelCommand::class)
    fun cancel(command: FreightCancelCommand) {

    }
}