package boaentrega.gsl.transportation.domain.transfer.eventsourcing.command

import boaentrega.gsl.transportation.configuration.constants.EventSourcingBeanQualifiers
import boaentrega.gsl.transportation.domain.transfer.TransferService
import boaentrega.gsl.transportation.support.eventsourcing.connectors.ConsumerConnector
import boaentrega.gsl.transportation.support.eventsourcing.controller.AbstractConsumerController
import boaentrega.gsl.transportation.support.eventsourcing.controller.annotations.ConsumptionHandler
import gsl.schemas.*
import org.springframework.beans.factory.annotation.Qualifier
import org.springframework.stereotype.Component

@Component
class FreightCommandController(
        @Qualifier(EventSourcingBeanQualifiers.FREIGHT_COMMAND_CONSUMER)
        consumerConnector: ConsumerConnector,
        private val transferService: TransferService) : AbstractConsumerController(consumerConnector) {

 @ConsumptionHandler(FreightMovePackageCommand::class)
    fun movePackage(command: FreightMovePackageCommand) {
        transferService.create(command.trackId, command.orderId, command.freightId,
                command.currentPosition, command.deliveryAddress)
    }
}