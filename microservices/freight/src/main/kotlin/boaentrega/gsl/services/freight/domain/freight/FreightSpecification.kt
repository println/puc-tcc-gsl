package boaentrega.gsl.services.freight.domain.freight

import org.springframework.data.jpa.domain.Specification
import java.util.*
import javax.persistence.criteria.CriteriaBuilder
import javax.persistence.criteria.CriteriaQuery
import javax.persistence.criteria.Root


object FreightSpecification {
    fun order(age: UUID): Specification<Freight>? {
        return Specification { root: Root<Freight>, _: CriteriaQuery<*>, criteriaBuilder: CriteriaBuilder ->
            criteriaBuilder.equal(root.get<Any>(Freight::orderId.name), age)
        }
    }
}