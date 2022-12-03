package boaentrega.gsl.order.domain.delivery.web

import boaentrega.gsl.order.domain.delivery.Delivery
import boaentrega.gsl.order.domain.delivery.DeliveryFilter
import boaentrega.gsl.order.domain.delivery.DeliveryService
import boaentrega.gsl.support.web.AbstractWebService
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.stereotype.Service
import java.time.LocalTime
import java.util.*

@Service
class DeliveryWebService(private val service: DeliveryService) : AbstractWebService<Delivery>() {
    fun findAll(filter: DeliveryFilter, pageable: Pageable): Page<Delivery> {
        return service.findAll(filter, pageable)
    }

    fun findById(id: UUID): Delivery {
        val nullableEntity = service.findById(id)
        return assertNotFound(nullableEntity)
    }

    fun addPreferredTimeForDelivery(deliveryId: UUID, time: LocalTime): Delivery {
        val nullableEntity = service.addPreferredTimeForDelivery(deliveryId, time)
        return assertBadRequest(nullableEntity)
    }

    fun takePackageToDelivery(deliveryId: UUID, partnerId: UUID): Delivery {
        val nullableEntity = service.takePackageToDelivery(deliveryId, partnerId)
        return assertBadRequest(nullableEntity)
    }

    fun giveBackPackageToRetryDelivery(deliveryId: UUID): Delivery {
        val nullableEntity = service.giveBackPackageToRetryDelivery(deliveryId)
        return assertBadRequest(nullableEntity)
    }

    fun markAsSuccessfulDelivery(deliveryId: UUID): Delivery {
        val nullableEntity = service.markAsSuccessfulDelivery(deliveryId)
        return assertBadRequest(nullableEntity)
    }

    fun markAsDeliveryFailed(deliveryId: UUID): Delivery {
        val nullableEntity = service.markAsDeliveryFailed(deliveryId)
        return assertBadRequest(nullableEntity)
    }
}