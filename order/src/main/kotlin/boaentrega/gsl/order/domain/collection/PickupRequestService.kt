package boaentrega.gsl.order.domain.collection

import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.data.jpa.domain.Specification
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.util.*

@Service
class PickupRequestService(
        private val repository: PickupRequestRepository,
        private val messenger: PickupRequestMessenger) {

    fun findAll(filter: PickupRequestFilter, pageable: Pageable): Page<PickupRequest> {
        var specification: Specification<PickupRequest> = Specification.where(null)
        filter.freightId?.let { specification = specification.and(PickupRequestSpecification.freight(it)) }
        return repository.findAll(specification, pageable)
    }

    fun findById(pickupRequestId: UUID): PickupRequest? {
        return repository.findById(pickupRequestId).orElse(null)
    }

    @Transactional
    fun createPickupRequest(
            trackId: UUID, orderId: UUID, freightId: UUID,
            pickupAddress: String, destination: String): Optional<PickupRequest> {

        if (repository.existsByTrackIdOrOrderIdOrFreightId(trackId, orderId, freightId)) {
            return Optional.empty()
        }

        val pickupRequest = PickupRequest(trackId, orderId, freightId, pickupAddress, destination)
        val entity = repository.save(pickupRequest)
        messenger.createPickupRequest(entity)
        return Optional.of(entity)
    }

    @Transactional
    fun markAsOutToPickupTheProduct(pickupRequestId: UUID, collectorEmployee: String): PickupRequest? {
        val entity = findById(pickupRequestId)
        if(!PickupRequestValidations.canPickup(entity, collectorEmployee)){
            return null
        }
        entity!!.status = PickupRequestStatus.PICKUP_PROCESS
        entity.collectorEmployee = collectorEmployee
        val updatedEntity = repository.save(entity)
        messenger.markAsOutToPickupTheProduct(updatedEntity)
        return updatedEntity
    }

    @Transactional
    fun markAsTaken(pickupRequestId: UUID, packageAddress: String): PickupRequest? {
        val entity = findById(pickupRequestId)
        if(!PickupRequestValidations.canTaken(entity, packageAddress)){
            return null
        }
        entity!!.status = PickupRequestStatus.TAKEN
        entity.currentPosition = packageAddress
        val updatedEntity = repository.save(entity)
        messenger.markAsTaken(updatedEntity)
        return updatedEntity
    }

    @Transactional
    fun markAsOnPackaging(pickupRequestId: UUID, packerEmployee: String): PickupRequest? {
        val entity = findById(pickupRequestId)
        if(!PickupRequestValidations.canPackaging(entity, packerEmployee)){
            return null
        }
        entity!!.status = PickupRequestStatus.ON_PACKAGING
        entity.packerEmployee = packerEmployee
        val updatedEntity = repository.save(entity)
        messenger.markAsOnPackaging(updatedEntity)
        return updatedEntity
    }

    @Transactional
    fun markAsReadyToStartDelivery(pickupRequestId: UUID, dispenserAddress: String): PickupRequest? {
        val entity = findById(pickupRequestId)
        if(!PickupRequestValidations.isReady(entity, dispenserAddress)){
            return null
        }
        entity!!.status = PickupRequestStatus.FINISHED
        entity.currentPosition = dispenserAddress
        val updatedEntity = repository.save(entity)
        messenger.markAsReadyToStartDelivery(updatedEntity)
        return updatedEntity
    }

}
