package boaentrega.gsl.order.domain.freight

import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.data.jpa.domain.Specification
import org.springframework.http.HttpStatus
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import org.springframework.web.server.ResponseStatusException
import java.time.Instant
import java.util.*

@Service
class FreightService(
        private val repository: FreightRepository,
        private val messenger: FreightMessenger) {

    private companion object {
        val BLOCKED_STATUS_TRANSITIONS = listOf(
                FreightStatus.CREATED,
                FreightStatus.CANCELING,
                FreightStatus.CANCELED,
                FreightStatus.FINISHED
        )
    }

    fun findAll(filter: FreightFilter, pageable: Pageable): Page<Freight> {
        var specification: Specification<Freight> = Specification.where(null)
        filter.orderId?.let { specification = specification.and(FreightSpecification.order(it)) }
        return repository.findAll(specification, pageable)
    }

    fun findById(id: UUID): Freight {
        val entityOptional = repository.findById(id)
        if (entityOptional.isEmpty) {
            throw ResponseStatusException(HttpStatus.NOT_FOUND, "entity not found")
        }
        return entityOptional.get()
    }

    @Transactional
    fun createFreight(trackId: UUID, orderId: UUID, senderAddress: String, deliveryAddress: String): Optional<Freight> {

        if (repository.existsByTrackIdOrOrderId(trackId, orderId)) {
            return Optional.empty()
        }

        val freight = Freight(
                trackId = trackId,
                orderId = orderId,
                senderAddress = senderAddress,
                deliveryAddress = deliveryAddress,
                currentPosition = senderAddress)
        val entity = repository.save(freight)
        messenger.createFreight(entity)
        return Optional.of(entity)
    }

    @Transactional
    fun updateStatus(trackId: UUID, id: UUID, status: FreightStatus, currentPosition: String, lastUpdated: Instant) {
        if (BLOCKED_STATUS_TRANSITIONS.contains(status)) {
            return
        }

        val entityOptional = repository.findById(id)

        entityOptional.ifPresent {
            if(it.status == status){
                return@ifPresent
            }
            if(it.status == FreightStatus.FINISHED){
                return@ifPresent
            }
            it.currentPosition = currentPosition
            it.status = status
            it.lastUpdated = lastUpdated
            val updatedEntity = repository.save(it)
            messenger.updateStatus(updatedEntity)
        }
    }

    @Transactional
    fun deliverySuccessfully(id: UUID, lastUpdated: Instant) {
        val entityOptional = repository.findById(id)
        entityOptional.ifPresent {
            if(it.status == FreightStatus.DELIVERY_SUCCESS){
                return@ifPresent
            }
            if(it.status == FreightStatus.FINISHED){
                return@ifPresent
            }
            it.status = FreightStatus.DELIVERY_SUCCESS
            it.lastUpdated = lastUpdated
            it.currentPosition = it.deliveryAddress
            val updatedEntity = repository.save(it)
            messenger.updateStatus(updatedEntity)
        }
    }

    @Transactional
    fun finishFreight(id: UUID) {
        val entityOptional = repository.findById(id)
        entityOptional.ifPresent {
            if(it.status == FreightStatus.FINISHED){
                return@ifPresent
            }
            it.status = FreightStatus.FINISHED
            it.currentPosition = it.deliveryAddress
            it.lastUpdated = Instant.now()
            val updatedEntity = repository.save(it)
            messenger.finishFreight(updatedEntity)
        }
    }
}