package boaentrega.gsl.services.order.domain.order.eventsourcing.command

import boaentrega.gsl.configuration.constants.EventSourcingBeanQualifiers
import boaentrega.gsl.services.order.domain.order.OrderService
import boaentrega.gsl.support.eventsourcing.connectors.ConsumerConnector
import boaentrega.gsl.support.eventsourcing.controller.AbstractConsumerController
import boaentrega.gsl.support.eventsourcing.controller.annotations.ConsumptionHandler
import gsl.schemas.OrderApproveCommand
import gsl.schemas.OrderRefuseCommand
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