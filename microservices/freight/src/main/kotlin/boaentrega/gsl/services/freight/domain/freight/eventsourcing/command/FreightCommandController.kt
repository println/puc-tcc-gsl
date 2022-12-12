package boaentrega.gsl.services.freight.domain.freight.eventsourcing.command

import boaentrega.gsl.configuration.constants.EventSourcingBeanQualifiers
import boaentrega.gsl.services.freight.configuration.constants.Qualifiers
import boaentrega.gsl.services.freight.domain.freight.FreightService
import boaentrega.gsl.support.eventsourcing.connectors.ConsumerConnector
import boaentrega.gsl.support.eventsourcing.controller.AbstractConsumerController
import boaentrega.gsl.support.eventsourcing.controller.annotations.ConsumptionHandler
import gsl.schemas.*
import org.springframework.beans.factory.annotation.Qualifier
import org.springframework.stereotype.Component

@Component
@Qualifier(Qualifiers.APP_COMMAND_CONTROLLER)
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