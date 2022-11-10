package boaentrega.gsl.order.domain.delivery.web

import boaentrega.gsl.order.configuration.constants.ServiceNames
import boaentrega.gsl.order.domain.delivery.Delivery
import boaentrega.gsl.order.domain.delivery.DeliveryFilter
import boaentrega.gsl.order.domain.delivery.DeliveryService
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.web.bind.annotation.*
import java.util.*

@RequestMapping(ServiceNames.DELIVERY)
@RestController
class DeliveryController(private val service: DeliveryService) {

    @GetMapping
    fun getAll(
            @RequestParam(required = false) coordinates: String?,
            pageable: Pageable): Page<Delivery> {
        val filter = DeliveryFilter()
        return service.findAll(filter, pageable)
    }

    @GetMapping("/{id}")
    fun getById(
            @PathVariable("id") id: UUID): Delivery {
        return service.findById(id)
    }
}