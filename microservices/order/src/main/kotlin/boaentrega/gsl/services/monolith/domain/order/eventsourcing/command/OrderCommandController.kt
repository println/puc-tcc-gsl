package boaentrega.gsl.services.monolith.domain.order.eventsourcing.command

import boaentrega.gsl.configuration.constants.EventSourcingBeanQualifiers
import boaentrega.gsl.services.monolith.domain.order.OrderService
import boaentrega.gsl.support.eventsourcing.connectors.ConsumerConnector
import boaentrega.gsl.support.eventsourcing.controller.AbstractConsumerController
import boaentrega.gsl.support.eventsourcing.controller.annotations.ConsumptionHandler
import boaentrega.gsl.schemas.OrderApproveCommand
import boaentrega.gsl.schemas.OrderRefuseCommand
import org.springframework.beans.factory.annotation.Qualifier
import org.springframework.stereotype.Component

@Component
class OrderCommandController(
        @Qualifier(EventSourcingBeanQualifiers.ORDER_COMMAND_CONSUMER)
        consumerConnector: ConsumerConnector,
        private val service: OrderService
) : AbstractConsumerController(consumerConnector) {

    @ConsumptionHandler(OrderApproveCommand::class)
    fun approve(command: OrderApproveCommand) {
        service.approvePayment(command.orderId, command.value)
    }

    @ConsumptionHandler(OrderRefuseCommand::class)
    fun refuse(command: OrderRefuseCommand) {
        service.refusePayment(command.orderId, command.reason)
    }
}