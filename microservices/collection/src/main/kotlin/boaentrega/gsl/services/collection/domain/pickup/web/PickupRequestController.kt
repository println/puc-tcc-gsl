package boaentrega.gsl.services.collection.domain.pickup.web

import boaentrega.gsl.services.collection.domain.pickup.PickupRequest
import boaentrega.gsl.services.collection.domain.pickup.PickupRequestFilter
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.web.bind.annotation.*
import java.util.*


@RequestMapping("\${boaentrega.gsl.api.path.collection:}")
@RestController
class PickupRequestController(
        private val service: PickupRequestWebService
) {
    @GetMapping
    fun getAll(
            @RequestParam(value = "freight", required = false) freightId: UUID?,
            pageable: Pageable): Page<PickupRequest> {
        val filter = PickupRequestFilter(freightId)
        return service.findAll(filter, pageable)
    }

    @GetMapping("/{id}")
    fun getById(
            @PathVariable("id") pickupRequestId: UUID): PickupRequest {
        return service.findById(pickupRequestId)
    }

    @PutMapping("/{id}/pickup")
    fun markAsOutToPickupTheProduct(
            @PathVariable("id") pickupRequestId: UUID,
            @RequestBody data: EmployeeDto): PickupRequest {
        return service.markAsOutToPickupTheProduct(pickupRequestId, data.employee)
    }

    @PutMapping("/{id}/taken")
    fun markAsTaken(
            @PathVariable("id") pickupRequestId: UUID,
            @RequestBody data: AddressDto): PickupRequest {
        return service.markAsTaken(pickupRequestId, data.address)
    }

    @PutMapping("/{id}/packaging")
    fun markAsOnPackaging(
            @PathVariable("id") pickupRequestId: UUID,
            @RequestBody data: EmployeeDto): PickupRequest {
        return service.markAsOnPackaging(pickupRequestId, data.employee)
    }

    @PutMapping("/{id}/ready")
    fun markAsReadyToStartDelivery(
            @PathVariable("id") pickupRequestId: UUID,
            @RequestBody data: AddressDto): PickupRequest {
        return service.markAsReadyToStartDelivery(pickupRequestId, data.address)
    }
}