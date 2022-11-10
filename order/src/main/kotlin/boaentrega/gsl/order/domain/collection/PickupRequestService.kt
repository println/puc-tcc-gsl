package boaentrega.gsl.order.domain.collection

import boaentrega.gsl.order.domain.order.eventsourcing.command.FreightCommandService
import boaentrega.gsl.order.domain.order.eventsourcing.event.FreightEventService
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.data.jpa.domain.Specification
import org.springframework.http.HttpStatus
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import org.springframework.web.server.ResponseStatusException
import java.util.*

@Service
class PickupRequestService(
        private val repository: PickupRequestRepository,
        private val eventService: FreightEventService,
        private val commandService: FreightCommandService) {

    companion object {
        const val SOURCE = "collector"
    }

    fun findAll(collectorRequestFilter: PickupRequestFilter, pageable: Pageable): Page<PickupRequest> {
        val specification: Specification<PickupRequest> = Specification.where(null)
        return repository.findAll(specification, pageable)
    }

    fun findById(pickupRequestId: UUID): PickupRequest {
        val entityOptional = repository.findById(pickupRequestId)
        if (entityOptional.isEmpty) {
            throw ResponseStatusException(HttpStatus.NOT_FOUND, "entity not found")
        }
        return entityOptional.get()
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
        eventService.notifyCollectionStarted(trackId, SOURCE, "Pickup process started")
        return Optional.of(entity)
    }

    @Transactional
    fun markAsOutToPickupTheProduct(pickupRequestId: UUID, collectorEmployee: String): PickupRequest {
        val entity = findById(pickupRequestId)
        entity.status = PickupRequestStatus.PICKUP_PROCESS
        entity.collectorEmployee = collectorEmployee
        repository.save(entity)
        eventService.notifyCollectionPickupOut(entity.trackId, SOURCE, "The employee went out to pick up the product from the customer")
        return entity
    }

    @Transactional
    fun markAsTaken(pickupRequestId: UUID): PickupRequest {
        val entity = findById(pickupRequestId)
        entity.status = PickupRequestStatus.TAKEN
        repository.save(entity)
        eventService.notifyCollectionPickupTaken(entity.trackId, SOURCE, "The product is already with us")
        return entity
    }

    @Transactional
    fun markAsOnPackaging(pickupRequestId: UUID, packerEmployee: String): PickupRequest {
        val entity = findById(pickupRequestId)
        entity.status = PickupRequestStatus.ON_PACKAGING
        entity.packerEmployee = packerEmployee
        repository.save(entity)
        eventService.notifyCollectionPackagePreparing(entity.trackId, SOURCE, "We are making the transfer package")
        return entity
    }

    @Transactional
    fun markAsReadyToStartDelivery(pickupRequestId: UUID, packageAddress: String): PickupRequest {
        val entity = findById(pickupRequestId)
        entity.status = PickupRequestStatus.FINISHED
        entity.packageAddress = packageAddress
        repository.save(entity)
        eventService.notifyCollectionPackageReadyToMove(entity.trackId, SOURCE, "All ready to start shipping")
        commandService.movePackage(entity.trackId, entity.orderId, entity.freightId, entity.packageAddress!!, entity.destination)
        return entity
    }

}
