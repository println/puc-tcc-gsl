package boaentrega.gsl.order.domain.order

import boaentrega.gsl.order.domain.order.eventsourcing.command.OrderCommandService
import boaentrega.gsl.order.domain.order.eventsourcing.event.OrderEventService
import gsl.schemas.FreightPurchaseCommand
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class OrderService(
        private val commandService: OrderCommandService,
        private val eventService: OrderEventService,
        private val repository: OrderRepository) {

    fun startFire(message: String) {
        commandService.send(FreightPurchaseCommand("client", "consumer", message))
    }

    fun applyCommand(command: FreightPurchaseCommand) {

    }

    @Transactional
    fun createOrder(order: Order): Order {
        val entity = repository.save(Order())
        eventService.notifyCreated(entity)
        return entity
    }

    fun findByIdAndUser(): Order {
        return Order()
    }

    fun findAllByUser(): List<Order> {
        return listOf()
    }
}