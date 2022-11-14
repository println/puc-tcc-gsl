package boaentrega.gsl.order.domain.order.web

import boaentrega.gsl.order.configuration.constants.ServiceNames
import boaentrega.gsl.order.domain.order.Order
import boaentrega.gsl.order.domain.order.OrderFilter
import boaentrega.gsl.order.domain.order.OrderService
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import org.springframework.web.server.ResponseStatusException
import org.springframework.web.servlet.support.ServletUriComponentsBuilder
import java.net.URI
import java.util.*

@RequestMapping(ServiceNames.ORDER)
@RestController
class OrderController(val service: OrderService) {
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
        val optionalOrder = service.approvePayment(id, data.value)
        if(optionalOrder.isEmpty){
            throw ResponseStatusException(HttpStatus.NOT_FOUND, "entity not found")
        }
        return optionalOrder.get()
    }

    @PutMapping("/{id}/refuse")
    fun tempRefuseOrder(
            @PathVariable("id") id: UUID): Order {
        val optionalOrder = service.refusePayment(id, "cant pay")
        if(optionalOrder.isEmpty){
            throw ResponseStatusException(HttpStatus.NOT_FOUND, "entity not found")
        }
        return optionalOrder.get()
    }
}