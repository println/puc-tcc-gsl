package boaentrega.gsl.order.domain.transportation

import boaentrega.gsl.order.domain.transportation.api.RouteService
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.data.jpa.domain.Specification
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.util.*

@Service
class TransferService(
        private val repository: TransferRepository,
        private val routeService: RouteService,
        private val messenger: TransferMessenger) {

    fun findAll(filter: TransferFilter, pageable: Pageable): Page<Transfer> {
        var specification: Specification<Transfer> = Specification.where(null)
        filter.freightId?.let { specification = specification.and(TransferSpecification.freight(it)) }
        return repository.findAll(specification, pageable)
    }

    fun findById(id: UUID): Transfer? {
        return repository.findById(id).orElse(null)
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
    fun movingToNextStorage(transferId: UUID, partnerId: UUID, storage: String): Transfer? {
        val entity = findById(transferId)
        if (!TransferValidations.canMove(entity, storage)) {
            return null
        }
        entity!!.partnerId = partnerId
        entity.status = TransferStatus.MOVING
        entity.currentPosition = "${entity.currentPosition.replace("-" + entity.nextStorage, "")}-${entity.nextStorage}"
        val updatedEntity = repository.save(entity)
        messenger.movingToNextStorage(updatedEntity)
        return updatedEntity
    }

    @Transactional
    fun receiveOnStorage(transferId: UUID, partnerId: UUID, storage: String): Transfer? {
        val entity = findById(transferId)
        return when {
            TransferValidations.canReceive(entity) -> receiveOnStorage(entity!!, partnerId)
            TransferValidations.canTerminate(entity) -> finishMovement(entity!!, partnerId)
            else -> null
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

}