package boaentrega.gsl.order.domain.order.eventsourcing.command

import boaentrega.gsl.order.configuration.constants.EventSourcingBeansConstants
import boaentrega.gsl.order.domain.order.OrderService
import boaentrega.gsl.order.support.eventsourcing.connectors.ConsumerConnector
import boaentrega.gsl.order.support.eventsourcing.controller.AbstractConsumerController
import boaentrega.gsl.order.support.eventsourcing.controller.annotations.ConsumptionHandler
import gsl.schemas.*
import org.springframework.beans.factory.annotation.Qualifier
import org.springframework.stereotype.Component

@Component
class OrderCommandController(
        @Qualifier(EventSourcingBeansConstants.ORDER_COMMAND_CONSUMER)
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