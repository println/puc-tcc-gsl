package boaentrega.gsl.delivery.replicas.customer

import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository
import java.util.*

@Repository
interface CustomerDocRepository : JpaRepository<CustomerDoc, UUID> {
}