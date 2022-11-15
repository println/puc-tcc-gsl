package boaentrega.gsl.order.domain.order.web

import boaentrega.gsl.order.domain.order.Order
import boaentrega.gsl.order.domain.order.OrderFilter
import boaentrega.gsl.order.domain.order.OrderService
import boaentrega.gsl.order.support.web.AbstractWebService
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.stereotype.Service
import java.math.BigDecimal
import java.util.*

@Service
class OrderWebService(private val service: OrderService): AbstractWebService<Order>() {
    fun findAll(filter: OrderFilter, pageable: Pageable): Page<Order> {
        return service.findAll(filter, pageable)
    }

    fun findById(id: UUID): Order {
        val nullableEntity = service.findById(id)
        return assertNotFound(nullableEntity)
    }

    fun createOrder(customerId: UUID, pickupAddress: String, deliveryAddress: String): Order {
        val nullableEntity = service.createOrder(customerId, pickupAddress, deliveryAddress)
        return assertBadRequest(nullableEntity)
    }

    fun approvePayment(orderId: UUID, value: BigDecimal): Order {
        val nullableEntity = service.approvePayment(orderId, value)
        return assertBadRequest(nullableEntity)
    }

    fun refusePayment(orderId: UUID, reason: String): Order {
        val nullableEntity = service.refusePayment(orderId, reason)
        return assertBadRequest(nullableEntity)
    }
}