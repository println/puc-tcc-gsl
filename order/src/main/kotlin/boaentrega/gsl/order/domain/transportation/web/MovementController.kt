package boaentrega.gsl.order.domain.transportation.web

import boaentrega.gsl.order.configuration.constants.ServiceNames
import boaentrega.gsl.order.domain.delivery.web.PartnerDto
import boaentrega.gsl.order.domain.transportation.Movement
import boaentrega.gsl.order.domain.transportation.MovementFilter
import boaentrega.gsl.order.domain.transportation.MovementService
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.web.bind.annotation.*
import java.util.*

@RequestMapping(ServiceNames.TRANSPORT)
@RestController
class MovementController(private val service: MovementService) {

    @GetMapping
    fun getAll(
            @RequestParam(required = false) coordinates: String?,
            pageable: Pageable): Page<Movement> {
        val filter = MovementFilter()
        return service.findAll(filter, pageable)
    }

    @GetMapping("/{id}")
    fun getById(
            @PathVariable("id") id: UUID): Movement {
        return service.findById(id)
    }

    @PutMapping("/{id}/transfer")
    fun movingPackageToNextStorage(@PathVariable("id") id: UUID,
                                   @RequestBody data: PartnerDto): Movement {
        return service.movingToNextStorage(id, data.partnerId)
    }

    @PutMapping("/{id}/receive")
    fun receivePackageOnStorage(@PathVariable("id") id: UUID,
                                @RequestBody data: PartnerDto): Movement {
        return service.receiveOnStorage(id, data.partnerId)
    }
}