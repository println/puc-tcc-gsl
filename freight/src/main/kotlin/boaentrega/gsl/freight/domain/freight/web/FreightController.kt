package boaentrega.gsl.freight.domain.freight.web

import boaentrega.gsl.freight.configuration.constants.ServiceNames
import boaentrega.gsl.freight.domain.freight.Freight
import boaentrega.gsl.freight.domain.freight.FreightFilter
import boaentrega.gsl.freight.domain.freight.FreightService
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.web.bind.annotation.*
import java.util.*

@RequestMapping(ServiceNames.FREIGHT)
@RestController
class FreightController(val service: FreightService) {

    @GetMapping
    fun getAll(
            @RequestParam(value = "order", required = false) orderId: UUID?,
            pageable: Pageable): Page<Freight> {
        val filter = FreightFilter(orderId)
        return service.findAll(filter, pageable)
    }

    @GetMapping("/{id}")
    fun getById(
            @PathVariable("id") id: UUID): Freight {
        return service.findById(id)
    }
}