package boaentrega.gsl.delivery.replicas.freight

import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository
import java.util.*

@Repository
interface FreightDocRepository : JpaRepository<FreightDoc, UUID> {
}