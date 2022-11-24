package boaentrega.gsl.order.domain.transportation.web

import boaentrega.gsl.order.configuration.constants.ServiceNames
import boaentrega.gsl.order.domain.transportation.Transfer
import boaentrega.gsl.order.domain.transportation.TransferFilter
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.web.bind.annotation.*
import java.util.*

@RequestMapping(ServiceNames.TRANSPORT)
@RestController
class TransferController(private val service: TransferWebService) {

    @GetMapping
    fun getAll(
            @RequestParam(value = "freight", required = false) freightId: UUID?,
            pageable: Pageable): Page<Transfer> {
        val filter = TransferFilter(freightId)
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
        return service.movingToNextStorage(id, data.partnerId, data.storage)
    }

    @PutMapping("/{id}/receive")
    fun receivePackageOnStorage(@PathVariable("id") id: UUID,
                                @RequestBody data: PartnerDto): Transfer {
        return service.receiveOnStorage(id, data.partnerId, data.storage)
    }
}