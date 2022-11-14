package boaentrega.gsl.order.domain.transportation

import boaentrega.gsl.order.domain.transportation.api.RouteService
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.data.jpa.domain.Specification
import org.springframework.http.HttpStatus
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import org.springframework.web.server.ResponseStatusException
import java.util.*

@Service
class MovementService(
        private val repository: MovementRepository,
        private val routeService: RouteService,
        private val messenger: MovementMessenger) {

    fun findAll(filter: MovementFilter, pageable: Pageable): Page<Movement> {
        val specification: Specification<Movement> = Specification.where(null)
        return repository.findAll(specification, pageable)
    }

    fun findById(id: UUID): Movement {
        val entityOptional = repository.findById(id)
        if (entityOptional.isEmpty) {
            throw ResponseStatusException(HttpStatus.NOT_FOUND, "entity not found")
        }
        return entityOptional.get()
    }

    @Transactional
    fun create(trackId: UUID, orderId: UUID, freightId: UUID,
               currentPosition: String, deliveryAddress: String): Optional<Movement> {

        if (repository.existsByTrackIdOrOrderIdOrFreightId(trackId, orderId, freightId)) {
            return Optional.empty()
        }

        val firstAndLastStorages = routeService.calculateInitialAndFinalStops(currentPosition, deliveryAddress)
        val movement = Movement(
                trackId = trackId,
                orderId = orderId,
                freightId = freightId,
                deliveryAddress = deliveryAddress,
                currentPosition = currentPosition,
                nextStorage = firstAndLastStorages.first,
                finalStorage = firstAndLastStorages.second)
        val entity = repository.save(movement)
        messenger.create(entity)
        return Optional.of(entity)
    }

    @Transactional
    fun movingToNextStorage(deliveryId: UUID, partnerId: UUID): Movement {
        val entity = findById(deliveryId)
        entity.partnerId = partnerId
        entity.status = MovementStatus.MOVING
        entity.currentPosition = "${entity.currentPosition}-${entity.nextStorage}"
        val updatedEntity = repository.save(entity)
        messenger.movingToNextStorage(updatedEntity)
        return updatedEntity
    }

    @Transactional
    fun receiveOnStorage(deliveryId: UUID, partnerId: UUID): Movement {
        val entity = findById(deliveryId)
        return when {
            entity.status == MovementStatus.END_OF_ROUTE -> entity
            hasNextStorage(entity) -> receiveOnStorage(entity, partnerId)
            else -> finishMovement(entity, partnerId)
        }
    }

    private fun receiveOnStorage(entity: Movement, partnerId: UUID): Movement {
        entity.partnerId = partnerId
        entity.currentPosition = entity.nextStorage
        entity.status = MovementStatus.IN_STORAGE
        entity.nextStorage = routeService.calculateNextStop(entity.currentPosition, entity.deliveryAddress)
        val updatedEntity = repository.save(entity)
        messenger.receiveOnStorage(updatedEntity)
        return updatedEntity
    }

    private fun finishMovement(entity: Movement, partnerId: UUID): Movement {
        entity.partnerId = partnerId
        entity.currentPosition = entity.nextStorage
        entity.status = MovementStatus.END_OF_ROUTE
        val updatedEntity = repository.save(entity)
        messenger.finishMovement(updatedEntity)
        return updatedEntity
    }

    private fun hasNextStorage(entity: Movement): Boolean {
        return entity.nextStorage != entity.finalStorage
    }

}