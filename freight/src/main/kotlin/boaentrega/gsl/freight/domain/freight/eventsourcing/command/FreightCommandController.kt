package boaentrega.gsl.freight.domain.freight.eventsourcing.command

import boaentrega.gsl.freight.configuration.constants.EventSourcingBeanQualifiers
import boaentrega.gsl.freight.domain.freight.FreightService
import boaentrega.gsl.freight.support.eventsourcing.connectors.ConsumerConnector
import boaentrega.gsl.freight.support.eventsourcing.controller.AbstractConsumerController
import boaentrega.gsl.freight.support.eventsourcing.controller.annotations.ConsumptionHandler
import gsl.schemas.*
import org.springframework.beans.factory.annotation.Qualifier
import org.springframework.stereotype.Component

@Component
class FreightCommandController(
        @Qualifier(EventSourcingBeanQualifiers.FREIGHT_COMMAND_CONSUMER)
        consumerConnector: ConsumerConnector,
        private val freightService: FreightService) : AbstractConsumerController(consumerConnector) {

    @ConsumptionHandler(FreightCreateCommand::class)
    fun create(command: FreightCreateCommand) {
        freightService.createFreight(command.trackId, command.orderId,
                command.senderAddress, command.deliveryAddress)
    }

    @ConsumptionHandler(FreightFinishCommand::class)
    fun finish(command: FreightFinishCommand) {
        freightService.finishFreight(command.freightId)
    }

    @ConsumptionHandler(FreightCancelCommand::class)
    fun cancel(command: FreightCancelCommand) {

    }
}