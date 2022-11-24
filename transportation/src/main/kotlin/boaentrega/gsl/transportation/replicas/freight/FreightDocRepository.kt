package boaentrega.gsl.transportation.replicas.freight

import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository
import java.util.*

@Repository
interface FreightDocRepository : JpaRepository<FreightDoc, UUID> {
}