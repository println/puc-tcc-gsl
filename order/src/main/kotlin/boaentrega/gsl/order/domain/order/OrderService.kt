package boaentrega.gsl.order.domain.order

import boaentrega.gsl.order.domain.order.eventsourcing.command.FreightCommandService
import boaentrega.gsl.order.domain.order.eventsourcing.event.FreightEventService
import boaentrega.gsl.order.domain.order.eventsourcing.event.OrderEventService
import org.springframework.data.domain.PageRequest
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.math.BigDecimal
import java.util.*

@Service
class OrderService(
        private val commandService: FreightCommandService,
        private val eventService: OrderEventService,
        private val repository: OrderRepository) {

    @Transactional
    fun createOrder(customerId: UUID, pickupAddress: String, deliveryAddress: String): Order {
        val order = Order(customerId, pickupAddress, deliveryAddress)
        val entity = repository.save(order)
        eventService.notifyOrderCreated(entity.id!!)
        return entity
    }

    @Transactional
    fun approvePayment(orderId: UUID, value: BigDecimal){
        val entityOptional = repository.findById(orderId)
        entityOptional.ifPresent {
            it.status = OrderStatus.ACCEPTED
            it.value = value
            repository.save(it)
            val orderId = it.id!!
            eventService.notifyOrderAccepted(orderId)
            commandService.create(orderId, orderId, it.pickupAddress, it.deliveryAddress)
        }
    }

    @Transactional
    fun refusePayment(orderId: UUID, reason: String){
        val entityOptional = repository.findById(orderId)
        entityOptional.ifPresent {
            it.status = OrderStatus.REFUSED
            it.comment = reason
            repository.save(it)
            eventService.notifyOrderRefused(it.id!!)
        }
    }

    fun findAllByIdAndCustomerId(customerId: UUID): List<Order> {
        return repository.findAllByCustomerId(customerId, PageRequest.of(0, 2))
    }
}