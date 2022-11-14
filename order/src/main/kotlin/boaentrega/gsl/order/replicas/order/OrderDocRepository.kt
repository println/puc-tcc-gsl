package boaentrega.gsl.order.replicas.order

import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository
import java.util.*

@Repository
interface OrderDocRepository : JpaRepository<OrderDoc, UUID> {
}