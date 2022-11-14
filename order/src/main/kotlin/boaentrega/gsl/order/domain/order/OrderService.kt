package boaentrega.gsl.order.domain.order

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
        private val repository: OrderRepository,
        private val messenger: OrderMessenger) {

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
        val order = Order(
                customerId = customerId,
                pickupAddress = pickupAddress,
                deliveryAddress = deliveryAddress)
        val entity = repository.save(order)
        messenger.create(entity)
        return entity
    }

    @Transactional
    fun approvePayment(id: UUID, value: BigDecimal): Optional<Order> {
        val entityOptional = repository.findById(id)
        if (entityOptional.isEmpty) {
            return entityOptional
        }

        val entity = entityOptional.get()
        if (entity.status == OrderStatus.ACCEPTED) {
            return entityOptional
        }

        entity.status = OrderStatus.ACCEPTED
        entity.value = value
        val updatedEntity = repository.save(entity)
        messenger.approvePayment(updatedEntity)
        return Optional.of(updatedEntity)
    }

    @Transactional
    fun refusePayment(id: UUID, reason: String): Optional<Order> {
        val entityOptional = repository.findById(id)
        if (entityOptional.isEmpty) {
            return entityOptional
        }

        val entity = entityOptional.get()
        if (entity.status == OrderStatus.REFUSED) {
            return entityOptional
        }

        entity.status = OrderStatus.REFUSED
        entity.comment = reason
        val updatedEntity = repository.save(entity)
        messenger.refusePayment(updatedEntity)
        return Optional.of(updatedEntity)
    }

}