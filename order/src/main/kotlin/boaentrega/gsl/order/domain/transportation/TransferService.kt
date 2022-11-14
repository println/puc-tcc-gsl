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
class TransferService(
        private val repository: TransferRepository,
        private val routeService: RouteService,
        private val messenger: TransferMessenger) {

    fun findAll(filter: TransferFilter, pageable: Pageable): Page<Transfer> {
        val specification: Specification<Transfer> = Specification.where(null)
        return repository.findAll(specification, pageable)
    }

    fun findById(id: UUID): Transfer {
        val entityOptional = repository.findById(id)
        if (entityOptional.isEmpty) {
            throw ResponseStatusException(HttpStatus.NOT_FOUND, "entity not found")
        }
        return entityOptional.get()
    }

    @Transactional
    fun create(trackId: UUID, orderId: UUID, freightId: UUID,
               currentPosition: String, deliveryAddress: String): Optional<Transfer> {

        if (repository.existsByTrackIdOrOrderIdOrFreightId(trackId, orderId, freightId)) {
            return Optional.empty()
        }

        val firstAndLastStorages = routeService.calculateInitialAndFinalStops(currentPosition, deliveryAddress)
        val movement = Transfer(
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
    fun movingToNextStorage(transferId: UUID, partnerId: UUID): Transfer {
        val entity = findById(transferId)
        entity.partnerId = partnerId
        entity.status = TransferStatus.MOVING
        entity.currentPosition = "${entity.currentPosition}-${entity.nextStorage}"
        val updatedEntity = repository.save(entity)
        messenger.movingToNextStorage(updatedEntity)
        return updatedEntity
    }

    @Transactional
    fun receiveOnStorage(transferId: UUID, partnerId: UUID): Transfer {
        val entity = findById(transferId)
        return when {
            entity.status == TransferStatus.END_OF_ROUTE -> entity
            hasNextStorage(entity) -> receiveOnStorage(entity, partnerId)
            else -> finishMovement(entity, partnerId)
        }
    }

    private fun receiveOnStorage(entity: Transfer, partnerId: UUID): Transfer {
        entity.partnerId = partnerId
        entity.currentPosition = entity.nextStorage
        entity.status = TransferStatus.IN_STORAGE
        entity.nextStorage = routeService.calculateNextStop(entity.currentPosition, entity.deliveryAddress)
        val updatedEntity = repository.save(entity)
        messenger.receiveOnStorage(updatedEntity)
        return updatedEntity
    }

    private fun finishMovement(entity: Transfer, partnerId: UUID): Transfer {
        entity.partnerId = partnerId
        entity.currentPosition = entity.nextStorage
        entity.status = TransferStatus.END_OF_ROUTE
        val updatedEntity = repository.save(entity)
        messenger.finishMovement(updatedEntity)
        return updatedEntity
    }

    private fun hasNextStorage(entity: Transfer): Boolean {
        return entity.nextStorage != entity.finalStorage
    }

}