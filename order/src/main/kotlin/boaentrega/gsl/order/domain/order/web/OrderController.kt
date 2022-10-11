package boaentrega.gsl.order.domain.order.web

import boaentrega.gsl.order.domain.order.OrderService
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RestController
import java.util.*

@RestController
class OrderController(val service: OrderService) {
    @GetMapping
    fun checkEvents(): String {
        val date = Date().toString()
        service.startFire(date)
        return date
    }
}