package boaentrega.gsl.delivery.domain.delivery.web

import boaentrega.gsl.delivery.domain.delivery.Delivery
import boaentrega.gsl.delivery.domain.delivery.DeliveryFilter
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.web.bind.annotation.*
import java.util.*

@RequestMapping
@RestController
class DeliveryController(private val service: DeliveryWebService) {

    @GetMapping
    fun getAll(
            @RequestParam(value = "freight", required = false) freightId: UUID?,
            pageable: Pageable): Page<Delivery> {
        val filter = DeliveryFilter(freightId)
        return service.findAll(filter, pageable)
    }

    @GetMapping("/{id}")
    fun getById(
            @PathVariable("id") id: UUID): Delivery {
        return service.findById(id)
    }

    @PutMapping("/{id}/time")
    fun addPreferredTimeForDelivery(
            @PathVariable("id") deliveryId: UUID,
            @RequestBody data: PreferredTimeDto): Delivery {
        return service.addPreferredTimeForDelivery(deliveryId, data.time)
    }

    @PutMapping("/{id}/package")
    fun takePackageToDelivery(
            @PathVariable("id") deliveryId: UUID,
            @RequestBody data: PartnerDto): Delivery {
        return service.takePackageToDelivery(deliveryId, data.partnerId)
    }

    @DeleteMapping("/{id}/package")
    fun returnPackage(
            @PathVariable("id") deliveryId: UUID): Delivery {
        return service.giveBackPackageToRetryDelivery(deliveryId)
    }

    @PutMapping("/{id}/delivery")
    fun markAsDeliverySuccessfully(
            @PathVariable("id") deliveryId: UUID): Delivery {
        return service.markAsSuccessfulDelivery(deliveryId)
    }

    @DeleteMapping("/{id}/delivery")
    fun markAsDeliveryFailed(
            @PathVariable("id") deliveryId: UUID): Delivery {
        return service.markAsDeliveryFailed(deliveryId)
    }
}