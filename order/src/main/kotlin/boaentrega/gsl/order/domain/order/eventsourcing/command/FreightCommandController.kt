package boaentrega.gsl.order.domain.order.eventsourcing.command

import boaentrega.gsl.order.configuration.constants.EventSourcingBeansConstants
import boaentrega.gsl.order.domain.collector.PickupRequestService
import boaentrega.gsl.order.domain.order.OrderService
import boaentrega.gsl.order.support.eventsourcing.connectors.ConsumerConnector
import boaentrega.gsl.order.support.eventsourcing.controller.AbstractConsumerController
import boaentrega.gsl.order.support.eventsourcing.controller.annotations.ConsumptionHandler
import gsl.schemas.*
import org.springframework.beans.factory.annotation.Qualifier
import org.springframework.stereotype.Component

@Component
class FreightCommandController(
        @Qualifier(EventSourcingBeansConstants.FREIGHT_COMMAND_CONSUMER)
        consumerConnector: ConsumerConnector,
        private val pickupRequestService: PickupRequestService
) : AbstractConsumerController(consumerConnector) {

    @ConsumptionHandler(FreightCreateCommand::class)
    fun create(command: FreightCreateCommand) {

    }

    @ConsumptionHandler(FreightCreateCommand::class)
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

    }

    @ConsumptionHandler(FreightCancelCommand::class)
    fun cancel(command: FreightCancelCommand) {

    }
}