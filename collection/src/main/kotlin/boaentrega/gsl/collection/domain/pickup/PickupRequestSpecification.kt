package boaentrega.gsl.collection.domain.pickup

import org.springframework.data.jpa.domain.Specification
import java.util.*
import javax.persistence.criteria.CriteriaBuilder
import javax.persistence.criteria.CriteriaQuery
import javax.persistence.criteria.Root


object PickupRequestSpecification {
    fun freight(age: UUID): Specification<PickupRequest> {
        return Specification { root: Root<PickupRequest>, _: CriteriaQuery<*>, criteriaBuilder: CriteriaBuilder ->
            criteriaBuilder.equal(root.get<Any>(PickupRequest::freightId.name), age)
        }
    }
}