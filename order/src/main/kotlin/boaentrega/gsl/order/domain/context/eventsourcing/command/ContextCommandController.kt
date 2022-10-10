package boaentrega.gsl.order.domain.context.eventsourcing.command

import boaentrega.gsl.order.configuration.constants.EventSourcingBeansConstants
import boaentrega.gsl.order.domain.context.ContextService
import boaentrega.gsl.support.eventsourcing.connectors.ConsumerConnector
import boaentrega.gsl.support.eventsourcing.controller.AbstractConsumerController
import boaentrega.gsl.support.eventsourcing.controller.annotations.ConsumptionHandler
import gsl.schemas.FreightPurchaseCommand
import org.springframework.beans.factory.annotation.Qualifier
import org.springframework.stereotype.Component

@Component
class ContextCommandController(
        @Qualifier(EventSourcingBeansConstants.CONTEXT_COMMAND_CONSUMER)
        private val consumerConnector: ConsumerConnector,
        private val service: ContextService
) : AbstractConsumerController(consumerConnector) {

    @ConsumptionHandler(FreightPurchaseCommand::class)
    fun receive(command: FreightPurchaseCommand) {
        println(command)
        service.applyCommand(command)
    }
}