package boaentrega.gsl.order.domain.freight

import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.JpaSpecificationExecutor
import org.springframework.stereotype.Repository
import java.util.*

@Repository
interface FreightRepository : JpaRepository<Freight, UUID>, JpaSpecificationExecutor<Freight> {
    fun existsByTrackIdOrOrderId(trackId: UUID, orderId: UUID): Boolean
}
