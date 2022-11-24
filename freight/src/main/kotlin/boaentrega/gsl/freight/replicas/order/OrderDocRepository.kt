package boaentrega.gsl.freight.replicas.order

import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository
import java.util.*

@Repository
interface OrderDocRepository : JpaRepository<OrderDoc, UUID> {
}