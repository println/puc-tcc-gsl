package boaentrega.gsl.order.domain.delivery

import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.data.jpa.domain.Specification
import org.springframework.http.HttpStatus
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import org.springframework.web.server.ResponseStatusException
import java.time.LocalTime
import java.util.*

@Service
class DeliveryService(
        private val repository: DeliveryRepository,
        private val messenger: DeliveryMessenger) {

    fun findAll(filter: DeliveryFilter, pageable: Pageable): Page<Delivery> {
        val specification: Specification<Delivery> = Specification.where(null)
        return repository.findAll(specification, pageable)
    }

    fun findById(id: UUID): Delivery {
        val entityOptional = repository.findById(id)
        if (entityOptional.isEmpty) {
            throw ResponseStatusException(HttpStatus.NOT_FOUND, "entity not found")
        }
        return entityOptional.get()
    }

    @Transactional
    fun create(
            trackId: UUID, orderId: UUID, freightId: UUID,
            pickupAddress: String, deliveryAddress: String): Optional<Delivery> {

        if (repository.existsByTrackIdOrOrderIdOrFreightId(trackId, orderId, freightId)) {
            return Optional.empty()
        }

        val delivery = Delivery(
                trackId = trackId,
                orderId = orderId,
                freightId = freightId,
                deliveryAddress = deliveryAddress,
                currentPosition = pickupAddress)
        val entity = repository.save(delivery)
        messenger.create(entity)
        return Optional.of(entity)
    }

    @Transactional
    fun addPreferredTimeForDelivery(deliveryId: UUID, preferredTime: LocalTime): Delivery {
        val entity = findById(deliveryId)
        entity.preferredDeliveryTime = preferredTime
        return repository.save(entity)
    }

    @Transactional
    fun takePackageToDelivery(deliveryId: UUID, partnerId: UUID): Delivery {
        val entity = findById(deliveryId)
        entity.partnerId = partnerId
        entity.status = DeliveryStatus.OUT_FOR_DELIVERY
        val updatedEntity = repository.save(entity)
        messenger.takePackageToDelivery(updatedEntity)
        return updatedEntity
    }

    @Transactional
    fun giveBackPackageToRetryDelivery(deliveryId: UUID): Delivery {
        val entity = findById(deliveryId)
        entity.status = DeliveryStatus.RETRY_DELIVERY
        val updatedEntity = repository.save(entity)
        messenger.giveBackPackageToRetryDelivery(updatedEntity)
        return updatedEntity
    }

    @Transactional
    fun markAsSuccessfulDelivery(deliveryId: UUID): Delivery {
        val entity = findById(deliveryId)
        entity.status = DeliveryStatus.SUCCESSFULLY_DELIVERED
        entity.currentPosition = entity.deliveryAddress
        val updatedEntity = repository.save(entity)
        messenger.markAsSuccessfulDelivery(updatedEntity)
        return updatedEntity
    }

    @Transactional
    fun markAsDeliveryFailed(deliveryId: UUID): Delivery {
        val entity = findById(deliveryId)
        entity.status = DeliveryStatus.FAILED_DELIVERY_ATTEMPT
        val updatedEntity = repository.save(entity)
        messenger.markAsDeliveryFailed(updatedEntity)
        return updatedEntity
    }
}