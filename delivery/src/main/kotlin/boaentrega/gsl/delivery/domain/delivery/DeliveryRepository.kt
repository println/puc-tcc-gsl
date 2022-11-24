package boaentrega.gsl.delivery.domain.delivery

import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.JpaSpecificationExecutor
import java.util.*

interface DeliveryRepository : JpaRepository<Delivery, UUID>, JpaSpecificationExecutor<Delivery> {
    fun existsByTrackIdOrOrderIdOrFreightId(trackId: UUID, orderId: UUID, freightId: UUID): Boolean
}
