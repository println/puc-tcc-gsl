package boaentrega.gsl.delivery.domain.delivery.eventsourcing.command

import boaentrega.gsl.delivery.configuration.constants.EventSourcingBeanQualifiers
import boaentrega.gsl.delivery.domain.collection.PickupRequestService
import boaentrega.gsl.delivery.domain.delivery.DeliveryService
import boaentrega.gsl.delivery.domain.freight.FreightService
import boaentrega.gsl.delivery.domain.transportation.TransferService
import boaentrega.gsl.delivery.support.eventsourcing.connectors.ConsumerConnector
import boaentrega.gsl.delivery.support.eventsourcing.controller.AbstractConsumerController
import boaentrega.gsl.delivery.support.eventsourcing.controller.annotations.ConsumptionHandler
import gsl.schemas.*
import org.springframework.beans.factory.annotation.Qualifier
import org.springframework.stereotype.Component

@Component
class FreightCommandController(
        @Qualifier(EventSourcingBeanQualifiers.FREIGHT_COMMAND_CONSUMER)
        consumerConnector: ConsumerConnector,
        private val freightService: FreightService,
        private val pickupRequestService: PickupRequestService,
        private val deliveryService: DeliveryService,
        private val transferService: TransferService) : AbstractConsumerController(consumerConnector) {

    @ConsumptionHandler(FreightCreateCommand::class)
    fun create(command: FreightCreateCommand) {
        freightService.createFreight(command.trackId, command.orderId,
                command.senderAddress, command.deliveryAddress)
    }

    @ConsumptionHandler(FreightPickupProductCommand::class)
    fun pickupProduct(command: FreightPickupProductCommand) {
        pickupRequestService.createPickupRequest(command.trackId, command.orderId, command.freightId,
                command.pickupAddress, command.deliveryAddress)
    }

    @ConsumptionHandler(FreightMovePackageCommand::class)
    fun movePackage(command: FreightMovePackageCommand) {
        transferService.create(command.trackId, command.orderId, command.freightId,
                command.currentPosition, command.deliveryAddress)
    }

    @ConsumptionHandler(FreightDeliverPackageCommand::class)
    fun deliverPackage(command: FreightDeliverPackageCommand) {
        deliveryService.create(command.trackId, command.orderId, command.freightId,
                command.currentPosition, command.deliveryAddress)
    }

    @ConsumptionHandler(FreightFinishCommand::class)
    fun finish(command: FreightFinishCommand) {
        freightService.finishFreight(command.freightId)
    }

    @ConsumptionHandler(FreightCancelCommand::class)
    fun cancel(command: FreightCancelCommand) {

    }
}