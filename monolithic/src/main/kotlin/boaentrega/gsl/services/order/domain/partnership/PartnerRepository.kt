package boaentrega.gsl.services.order.domain.partnership

import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.JpaSpecificationExecutor
import java.util.*

interface PartnerRepository : JpaRepository<Partner, UUID>, JpaSpecificationExecutor<Partner> {

}
