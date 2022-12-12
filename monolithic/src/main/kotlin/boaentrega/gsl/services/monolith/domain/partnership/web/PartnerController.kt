package boaentrega.gsl.services.monolith.domain.partnership.web

import boaentrega.gsl.configuration.constants.ServiceNames
import boaentrega.gsl.services.monolith.domain.partnership.Partner
import boaentrega.gsl.services.monolith.domain.partnership.PartnerFilter
import boaentrega.gsl.services.monolith.domain.partnership.PartnerService
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.web.bind.annotation.*
import java.util.*

@RequestMapping(ServiceNames.PARTNERSHIP)
@RestController
class PartnerController(val service: PartnerService) {

    @GetMapping
    fun getAll(
            @RequestParam(required = false) coordinates: String?,
            pageable: Pageable): Page<Partner> {
        val filter = PartnerFilter()
        return service.findAll(filter, pageable)
    }

    @GetMapping("/{id}")
    fun getById(
            @PathVariable("id") id: UUID): Partner {
        return service.findById(id)
    }
}