package boaentrega.gsl.order.domain.order

import org.springframework.data.domain.Pageable
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository
import java.util.*

@Repository
interface OrderRepository: JpaRepository<Order, UUID> {
    fun findAllByCustomerId(customerId: UUID, pageable: Pageable): List<Order>
}