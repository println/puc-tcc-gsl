package boaentrega.gsl.order.domain.order.web

import boaentrega.gsl.order.domain.order.Order
import boaentrega.gsl.order.domain.order.OrderService
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RestController
import org.springframework.web.servlet.support.ServletUriComponentsBuilder
import java.net.URI
import java.util.*


@RestController
class OrderController(val service: OrderService) {
    @GetMapping("test")
    fun checkEvents() {
        //service.sendCreateOrderCommand(UUID.randomUUID(), "pickupAddress", "deliveryAddress")
    }

    @PostMapping
    fun createOrder(): ResponseEntity<Order> {
        val entity = service.createOrder(UUID.randomUUID(), "pickupAddress", "deliveryAddress")

        val location: URI = ServletUriComponentsBuilder
                .fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(entity.id).toUri()

        return ResponseEntity.created(location).body(entity)
    }
}