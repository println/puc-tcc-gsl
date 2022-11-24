package boaentrega.gsl.delivery.domain.delivery

import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.data.jpa.domain.Specification
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.time.LocalTime
import java.util.*

@Service
class DeliveryService(
        private val repository: DeliveryRepository,
        private val messenger: DeliveryMessenger) {

    fun findAll(filter: DeliveryFilter, pageable: Pageable): Page<Delivery> {
        var specification: Specification<Delivery> = Specification.where(null)
        filter.freightId?.let { specification = specification.and(DeliverySpecification.freight(it)) }
        return repository.findAll(specification, pageable)
    }

    fun findById(id: UUID): Delivery? {
        return repository.findById(id).orElse(null)
    }

    @Transactional
    fun create(
            trackId: UUID, orderId: UUID, freightId: UUID,
            pickupAddress: String, deliveryAddress: String): Delivery? {

        if (repository.existsByTrackIdOrOrderIdOrFreightId(trackId, orderId, freightId)) {
            return null
        }

        val delivery = Delivery(
                trackId = trackId,
                orderId = orderId,
                freightId = freightId,
                storageAddress = pickupAddress,
                deliveryAddress = deliveryAddress,
                currentPosition = pickupAddress)
        val entity = repository.save(delivery)
        messenger.create(entity)
        return entity
    }

    @Transactional
    fun addPreferredTimeForDelivery(deliveryId: UUID, preferredTime: LocalTime): Delivery? {
        val entity = findById(deliveryId)
        if (!DeliveryValidations.canSchedule(entity, preferredTime)) {
            return null
        }
        entity!!.preferredDeliveryTime = preferredTime
        return repository.save(entity)
    }

    @Transactional
    fun takePackageToDelivery(deliveryId: UUID, partnerId: UUID): Delivery? {
        val entity = findById(deliveryId)
        if (!DeliveryValidations.canDelivery(entity)) {
            return null
        }
        entity!!.partnerId = partnerId
        entity.status = DeliveryStatus.OUT_FOR_DELIVERY
        entity.currentPosition = "${entity.storageAddress}-${entity.deliveryAddress}"
        val updatedEntity = repository.save(entity)
        messenger.takePackageToDelivery(updatedEntity)
        return updatedEntity
    }

    @Transactional
    fun giveBackPackageToRetryDelivery(deliveryId: UUID): Delivery? {
        val entity = findById(deliveryId)
        if (!DeliveryValidations.canReturnPackageToRetry(entity)) {
            return null
        }
        entity!!.status = DeliveryStatus.RETRY_DELIVERY
        entity.currentPosition = entity.storageAddress
        val updatedEntity = repository.save(entity)
        messenger.giveBackPackageToRetryDelivery(updatedEntity)
        return updatedEntity
    }

    @Transactional
    fun markAsSuccessfulDelivery(deliveryId: UUID): Delivery? {
        val entity = findById(deliveryId)
        if (!DeliveryValidations.isSuccessful(entity)) {
            return null
        }
        entity!!.status = DeliveryStatus.SUCCESSFULLY_DELIVERED
        entity.currentPosition = entity.deliveryAddress
        val updatedEntity = repository.save(entity)
        messenger.markAsSuccessfulDelivery(updatedEntity)
        return updatedEntity
    }

    @Transactional
    fun markAsDeliveryFailed(deliveryId: UUID): Delivery? {
        val entity = findById(deliveryId)
        if (!DeliveryValidations.canFailed(entity)) {
            return null
        }
        entity!!.status = DeliveryStatus.FAILED_DELIVERY_ATTEMPT
        entity.currentPosition = "${entity.deliveryAddress}-${entity.storageAddress}"
        val updatedEntity = repository.save(entity)
        messenger.markAsDeliveryFailed(updatedEntity)
        return updatedEntity
    }
}