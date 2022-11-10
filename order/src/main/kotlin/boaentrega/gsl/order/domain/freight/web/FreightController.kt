package boaentrega.gsl.order.domain.freight.web

import boaentrega.gsl.order.configuration.constants.ServiceNames
import boaentrega.gsl.order.domain.freight.Freight
import boaentrega.gsl.order.domain.freight.FreightFilter
import boaentrega.gsl.order.domain.freight.FreightService
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.web.bind.annotation.*
import java.util.*

@RequestMapping(ServiceNames.FREIGHT)
@RestController
class FreightController(val service: FreightService) {

    @GetMapping
    fun getAll(
            @RequestParam(required = false) coordinates: String?,
            pageable: Pageable): Page<Freight> {
        val filter = FreightFilter()
        return service.findAll(filter, pageable)
    }

    @GetMapping("/{id}")
    fun getById(
            @PathVariable("id") id: UUID): Freight {
        return service.findById(id)
    }
}