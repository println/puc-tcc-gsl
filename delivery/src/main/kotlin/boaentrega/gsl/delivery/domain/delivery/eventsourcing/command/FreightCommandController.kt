package boaentrega.gsl.delivery.domain.delivery.eventsourcing.command

import boaentrega.gsl.delivery.configuration.constants.EventSourcingBeanQualifiers
import boaentrega.gsl.delivery.domain.delivery.DeliveryService
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
        private val deliveryService: DeliveryService) : AbstractConsumerController(consumerConnector) {

    @ConsumptionHandler(FreightDeliverPackageCommand::class)
    fun deliverPackage(command: FreightDeliverPackageCommand) {
        deliveryService.create(command.trackId, command.orderId, command.freightId,
                command.currentPosition, command.deliveryAddress)
    }
}