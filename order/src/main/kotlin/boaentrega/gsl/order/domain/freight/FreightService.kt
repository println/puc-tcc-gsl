package boaentrega.gsl.order.domain.freight

import boaentrega.gsl.order.configuration.constants.ServiceNames
import boaentrega.gsl.order.domain.order.eventsourcing.command.FreightCommandService
import boaentrega.gsl.order.domain.order.eventsourcing.document.FreightDocumentBroadcastService
import boaentrega.gsl.order.domain.order.eventsourcing.event.FreightEventService
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
        private val freightEventService: FreightEventService,
        private val freightCommandService: FreightCommandService,
        private val freightDocumentBroadcastService: FreightDocumentBroadcastService) {

    private companion object {
        val BLOCKED_STATUS_TRANSITIONS = listOf<FreightStatus>(
                FreightStatus.CREATED,
                FreightStatus.CANCELING,
                FreightStatus.CANCELED,
                FreightStatus.FINISHED
        )
    }

    fun findAll(filter: FreightFilter, pageable: Pageable): Page<Freight> {
        val specification: Specification<Freight> = Specification.where(null)
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
    fun createFreight(trackId: UUID, orderId: UUID, from: String, to: String): Optional<Freight> {

        if (repository.existsByTrackIdOrOrderId(trackId, orderId)) {
            return Optional.empty()
        }

        val freight = Freight(trackId, orderId, from, to)
        val entity = repository.save(freight)
        freightEventService.notifyCreated(entity.trackId, entity.id!!, ServiceNames.FREIGHT, "Freight process has been created", entity.lastUpdated)
        freightCommandService.pickupProduct(entity.trackId, entity.orderId, entity.id!!, entity.addressFrom, entity.addressTo)
        freightDocumentBroadcastService.release(entity)
        return Optional.of(entity)
    }

    @Transactional
    fun updateStatus(trackId: UUID, id: UUID, status: FreightStatus, lastUpdated: Instant) {
        if (BLOCKED_STATUS_TRANSITIONS.contains(status)) {
            return
        }

        val entityOptional = repository.findById(id)
        entityOptional.ifPresent {
            if (!it.lastUpdated.isBefore(lastUpdated)) {
                return@ifPresent
            }

            it.status = status
            it.lastUpdated = lastUpdated
            val updatedEntity = repository.save(it)
            freightDocumentBroadcastService.release(updatedEntity)
        }
    }

    @Transactional
    fun finishFreight(id: UUID) {
        val entityOptional = repository.findById(id)
        entityOptional.ifPresent {
            if (it.status != FreightStatus.DELIVERY_SUCCESS) {
                return@ifPresent
            }

            it.status = FreightStatus.FINISHED
            it.lastUpdated = Instant.now()
            val updatedEntity = repository.save(it)
            freightEventService.notifyFinished(updatedEntity.trackId, updatedEntity.id!!, ServiceNames.FREIGHT, "Freight process has been created", updatedEntity.lastUpdated)
            freightDocumentBroadcastService.release(updatedEntity)
        }

    }
}