package boaentrega.gsl.transportation.domain.transfer

import org.springframework.data.jpa.domain.Specification
import java.util.*
import javax.persistence.criteria.CriteriaBuilder
import javax.persistence.criteria.CriteriaQuery
import javax.persistence.criteria.Root


object TransferSpecification {
    fun freight(age: UUID): Specification<Transfer> {
        return Specification { root: Root<Transfer>, _: CriteriaQuery<*>, criteriaBuilder: CriteriaBuilder ->
            criteriaBuilder.equal(root.get<Any>(Transfer::freightId.name), age)
        }
    }
}