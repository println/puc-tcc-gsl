package boaentrega.gsl.services.order.domain.order.web

import boaentrega.gsl.services.order.domain.order.Order
import boaentrega.gsl.services.order.domain.order.OrderFilter
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import org.springframework.web.servlet.support.ServletUriComponentsBuilder
import java.net.URI
import java.util.*

@RequestMapping("\${boaentrega.gsl.api.path.order:}")
@RestController
class OrderController(val service: OrderWebService) {
    @GetMapping("test")
    fun checkEvents() {
        //service.sendCreateOrderCommand(UUID.randomUUID(), "pickupAddress", "deliveryAddress")
    }

    @GetMapping
    fun getAll(
            @RequestParam(required = false) coordinates: String?,
            pageable: Pageable): Page<Order> {
        val filter = OrderFilter()
        return service.findAll(filter, pageable)
    }

    @GetMapping("/{id}")
    fun getById(
            @PathVariable("id") id: UUID): Order {
        return service.findById(id)
    }

    @PostMapping
    fun createOrder(
            @RequestBody data: OrderDto): ResponseEntity<Order> {
        val entity = service.createOrder(data.customerId, data.pickupAddress, data.deliveryAddress)

        val location: URI = ServletUriComponentsBuilder
                .fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(entity.id).toUri()

        return ResponseEntity.created(location).body(entity)
    }

    @PutMapping("/{id}/approve")
    fun tempApproveOrder(
            @PathVariable("id") id: UUID,
            @RequestBody data: PaymentDto): Order {
        return service.approvePayment(id, data.value)
    }

    @PutMapping("/{id}/refuse")
    fun tempRefuseOrder(
            @PathVariable("id") id: UUID): Order {
        return service.refusePayment(id, "cant pay")
    }
}