package boaentrega.gsl.order.domain.transportation

import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.JpaSpecificationExecutor
import java.util.*

interface TransferRepository : JpaRepository<Transfer, UUID>, JpaSpecificationExecutor<Transfer> {
    fun existsByTrackIdOrOrderIdOrFreightId(trackId: UUID, orderId: UUID, freightId: UUID): Boolean
}
