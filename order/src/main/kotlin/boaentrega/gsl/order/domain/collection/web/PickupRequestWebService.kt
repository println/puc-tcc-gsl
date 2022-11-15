package boaentrega.gsl.order.domain.collection.web

import boaentrega.gsl.order.domain.collection.PickupRequest
import boaentrega.gsl.order.domain.collection.PickupRequestFilter
import boaentrega.gsl.order.domain.collection.PickupRequestService
import boaentrega.gsl.order.support.web.AbstractWebService
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.stereotype.Service
import java.util.*

@Service
class PickupRequestWebService(private val service: PickupRequestService) : AbstractWebService<PickupRequest>() {
    fun findAll(filter: PickupRequestFilter, pageable: Pageable): Page<PickupRequest> {
        return service.findAll(filter, pageable)
    }

    fun findById(id: UUID): PickupRequest {
        val nullableEntity = service.findById(id)
        return assertNotFound(nullableEntity)
    }

    fun markAsOutToPickupTheProduct(pickupRequestId: UUID, employee: String): PickupRequest {
        val nullableEntity = service.markAsOutToPickupTheProduct(pickupRequestId, employee)
        return assertBadRequest(nullableEntity)
    }

    fun markAsTaken(pickupRequestId: UUID, address: String): PickupRequest {
        val nullableEntity = service.markAsTaken(pickupRequestId, address)
        return assertBadRequest(nullableEntity)
    }

    fun markAsOnPackaging(pickupRequestId: UUID, employee: String): PickupRequest {
        val nullableEntity = service.markAsOnPackaging(pickupRequestId, employee)
        return assertBadRequest(nullableEntity)
    }

    fun markAsReadyToStartDelivery(pickupRequestId: UUID, address: String): PickupRequest {
        val nullableEntity = service.markAsReadyToStartDelivery(pickupRequestId, address)
        return assertBadRequest(nullableEntity)
    }
}