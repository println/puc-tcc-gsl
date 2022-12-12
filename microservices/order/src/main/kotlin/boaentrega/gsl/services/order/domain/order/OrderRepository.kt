package boaentrega.gsl.services.order.domain.order

import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.data.jpa.domain.Specification
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.JpaSpecificationExecutor
import org.springframework.stereotype.Repository
import java.util.*

@Repository
interface OrderRepository : JpaRepository<Order, UUID>, JpaSpecificationExecutor<Order> {
    fun findAllByCustomerId(customerId: UUID, specification: Specification<Order>, pageable: Pageable): Page<Order>
}