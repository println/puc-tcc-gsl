package boaentrega.gsl.delivery.domain.delivery

import org.springframework.data.jpa.domain.Specification
import java.util.*
import javax.persistence.criteria.CriteriaBuilder
import javax.persistence.criteria.CriteriaQuery
import javax.persistence.criteria.Root


object DeliverySpecification {
    fun freight(age: UUID): Specification<Delivery> {
        return Specification { root: Root<Delivery>, _: CriteriaQuery<*>, criteriaBuilder: CriteriaBuilder ->
            criteriaBuilder.equal(root.get<Any>(Delivery::freightId.name), age)
        }
    }
}