package boaentrega.gsl.order.domain.collector.web

import boaentrega.gsl.order.domain.collector.PickupRequest
import boaentrega.gsl.order.domain.collector.PickupRequestFilter
import boaentrega.gsl.order.domain.collector.PickupRequestService
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.http.MediaType
import org.springframework.web.bind.annotation.*
import java.util.*


@RequestMapping("collector")
@RestController
class CollectorController(
        private val service: PickupRequestService
) {
    @GetMapping
    fun getAll(
            @RequestParam(required = false) coordinates: String?,
            pageable: Pageable): Page<PickupRequest> {
        val filter = PickupRequestFilter(coordinates)
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
            @PathVariable("id") pickupRequestId: UUID): PickupRequest {
        return service.markAsTaken(pickupRequestId)
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
            @RequestBody data: WithdrawAddressDto): PickupRequest {
        return service.markAsReadyToStartDelivery(pickupRequestId, data.address)
    }
}