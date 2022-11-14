package boaentrega.gsl.order.domain.transportation

import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.JpaSpecificationExecutor
import java.util.*

interface MovementRepository : JpaRepository<Movement, UUID>, JpaSpecificationExecutor<Movement> {
    fun existsByTrackIdOrOrderIdOrFreightId(trackId: UUID, orderId: UUID, freightId: UUID): Boolean
}
