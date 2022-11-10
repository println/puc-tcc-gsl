package boaentrega.gsl.order.domain.freight

import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.JpaSpecificationExecutor
import java.util.*

interface FreightRepository : JpaRepository<Freight, UUID>, JpaSpecificationExecutor<Freight> {

}
