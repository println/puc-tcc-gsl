package boaentrega.gsl.order.domain.customer

import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.JpaSpecificationExecutor
import java.util.*

interface CustomerRepository : JpaRepository<Customer, UUID>, JpaSpecificationExecutor<Customer> {

}
