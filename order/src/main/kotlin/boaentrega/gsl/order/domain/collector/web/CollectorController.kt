package boaentrega.gsl.order.domain.collector.web

import boaentrega.gsl.order.domain.collector.PickupRequest
import boaentrega.gsl.order.domain.collector.PickupRequestFilter
import boaentrega.gsl.order.domain.collector.PickupRequestService
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
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

    @PutMapping("/{id}/status/pickup")
    fun markAsOutToPickupTheProduct(
            @PathVariable("id") pickupRequestId: UUID, collectorEmployee: String): PickupRequest {
        return service.markAsOutToPickupTheProduct(pickupRequestId, collectorEmployee)
    }

    @PutMapping("/{id}/status/taken")
    fun markAsTaken(
            @PathVariable("id") pickupRequestId: UUID): PickupRequest {
        return service.markAsTaken(pickupRequestId)
    }

    @PutMapping("/{id}/status/packing")
    fun markAsOnPackaging(
            @PathVariable("id") pickupRequestId: UUID,
            @RequestParam("employee") packerEmployee: String): PickupRequest {
        return service.markAsOnPackaging(pickupRequestId, packerEmployee)
    }

    @PutMapping("/{id}/status/ready")
    fun markAsReadyToStartDelivery(
            @PathVariable("id") pickupRequestId: UUID,
            @RequestParam("address") packageAddress: String): PickupRequest {
        return service.markAsReadyToStartDelivery(pickupRequestId, packageAddress)
    }
}