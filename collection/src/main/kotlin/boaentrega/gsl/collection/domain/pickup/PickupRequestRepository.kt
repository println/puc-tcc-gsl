package boaentrega.gsl.collection.domain.pickup

import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.JpaSpecificationExecutor
import org.springframework.stereotype.Repository
import java.util.*

@Repository
interface PickupRequestRepository : JpaRepository<PickupRequest, UUID>, JpaSpecificationExecutor<PickupRequest> {
    fun existsByTrackIdOrOrderIdOrFreightId(trackId: UUID, orderId: UUID, freightId: UUID): Boolean
}