package boaentrega.gsl.order.domain.order

import boaentrega.gsl.order.domain.order.eventsourcing.command.FreightCommandService
import boaentrega.gsl.order.domain.order.eventsourcing.event.OrderEventService
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.data.jpa.domain.Specification
import org.springframework.http.HttpStatus
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import org.springframework.web.server.ResponseStatusException
import java.math.BigDecimal
import java.util.*

@Service
class OrderService(
        private val freightCommandService: FreightCommandService,
        private val orderEventService: OrderEventService,
        private val repository: OrderRepository) {

    fun findAll(orderFilter: OrderFilter, pageable: Pageable): Page<Order> {
        val specification: Specification<Order> = Specification.where(null)
        return repository.findAll(specification, pageable)
    }

    fun findAllByIdAndCustomerId(customerId: UUID, orderFilter: OrderFilter, pageable: Pageable): Page<Order> {
        val specification: Specification<Order> = Specification.where(null)
        return repository.findAllByCustomerId(customerId, specification, pageable)
    }

    fun findById(id: UUID): Order {
        val entityOptional = repository.findById(id)
        if (entityOptional.isEmpty) {
            throw ResponseStatusException(HttpStatus.NOT_FOUND, "entity not found")
        }
        return entityOptional.get()
    }

    @Transactional
    fun createOrder(customerId: UUID, pickupAddress: String, deliveryAddress: String): Order {
        val order = Order(customerId, pickupAddress, deliveryAddress)
        val entity = repository.save(order)
        orderEventService.notifyOrderCreated(entity.id!!)
        return entity
    }

    @Transactional
    fun approvePayment(id: UUID, value: BigDecimal){
        val entityOptional = repository.findById(id)
        entityOptional.ifPresent {
            if(it.status == OrderStatus.ACCEPTED){
                return@ifPresent
            }

            it.status = OrderStatus.ACCEPTED
            it.value = value
            repository.save(it)
            val orderId = it.id!!
            orderEventService.notifyOrderAccepted(orderId)
            freightCommandService.create(orderId, orderId, it.pickupAddress, it.deliveryAddress)
        }
    }

    @Transactional
    fun refusePayment(id: UUID, reason: String) {
        val entityOptional = repository.findById(id)
        entityOptional.ifPresent {
            if(it.status == OrderStatus.REFUSED){
                return@ifPresent
            }

            it.status = OrderStatus.REFUSED
            it.comment = reason
            repository.save(it)
            orderEventService.notifyOrderRefused(it.id!!)
        }
    }

}