package boaentrega.gsl.order.domain.transportation.web

import boaentrega.gsl.order.configuration.constants.ServiceNames
import boaentrega.gsl.order.domain.delivery.web.PartnerDto
import boaentrega.gsl.order.domain.transportation.Transfer
import boaentrega.gsl.order.domain.transportation.TransferFilter
import boaentrega.gsl.order.domain.transportation.TransferService
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.web.bind.annotation.*
import java.util.*

@RequestMapping(ServiceNames.TRANSPORT)
@RestController
class TransferController(private val service: TransferService) {

    @GetMapping
    fun getAll(
            @RequestParam(required = false) coordinates: String?,
            pageable: Pageable): Page<Transfer> {
        val filter = TransferFilter()
        return service.findAll(filter, pageable)
    }

    @GetMapping("/{id}")
    fun getById(
            @PathVariable("id") id: UUID): Transfer {
        return service.findById(id)
    }

    @PutMapping("/{id}/transfer")
    fun movingPackageToNextStorage(@PathVariable("id") id: UUID,
                                   @RequestBody data: PartnerDto): Transfer {
        return service.movingToNextStorage(id, data.partnerId)
    }

    @PutMapping("/{id}/receive")
    fun receivePackageOnStorage(@PathVariable("id") id: UUID,
                                @RequestBody data: PartnerDto): Transfer {
        return service.receiveOnStorage(id, data.partnerId)
    }
}