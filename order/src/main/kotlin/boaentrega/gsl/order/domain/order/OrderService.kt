package boaentrega.gsl.order.domain.order

import boaentrega.gsl.order.domain.order.eventsourcing.command.OrderCommandService
import boaentrega.gsl.order.domain.order.eventsourcing.event.OrderEventService
import gsl.schemas.FreightPurchaseCommand
import gsl.schemas.FreightPurchasedEvent
import org.springframework.stereotype.Service
import java.util.*

@Service
class OrderService(
        private val commandService: OrderCommandService,
        private val eventService: OrderEventService,
        private val repository: OrderRepository) {

    fun startFire(message: String) {
        commandService.send(FreightPurchaseCommand("client", "consumer", message))
    }

    fun applyCommand(command: FreightPurchaseCommand) {
        eventService.emit(FreightPurchasedEvent(command.client, command.consumer, command.product))
    }

    fun createOrder(data: Order): Order {
        repository.save(data)
        return Order()
    }

    fun findByIdAndUser(): Order {
        return Order()
    }

    fun findAllByUser(): List<Order> {
        return listOf()
    }
}