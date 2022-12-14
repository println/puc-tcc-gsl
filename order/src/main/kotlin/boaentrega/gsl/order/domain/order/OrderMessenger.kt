package boaentrega.gsl.order.domain.order

import boaentrega.gsl.order.domain.order.eventsourcing.command.FreightCommandService
import boaentrega.gsl.order.domain.order.eventsourcing.document.OrderDocumentBroadcastService
import boaentrega.gsl.order.domain.order.eventsourcing.event.OrderEventService
import org.springframework.stereotype.Component

@Component
class OrderMessenger(
        private val freightCommandService: FreightCommandService,
        private val orderEventService: OrderEventService,
        private val orderDocumentBroadcastService: OrderDocumentBroadcastService) {

    fun create(entity: Order) {
        orderEventService.notifyOrderCreated(entity.id!!)
        orderDocumentBroadcastService.release(entity)
    }

    fun approvePayment(entity: Order) {
        val orderId = entity.id!!
        orderEventService.notifyOrderAccepted(orderId)
        freightCommandService.create(orderId, orderId, entity.pickupAddress, entity.deliveryAddress)
        orderDocumentBroadcastService.release(entity)
    }

    fun refusePayment(entity: Order) {
        orderEventService.notifyOrderRefused(entity.id!!)
        orderDocumentBroadcastService.release(entity)
    }
}