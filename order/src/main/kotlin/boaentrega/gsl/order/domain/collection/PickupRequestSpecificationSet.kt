package boaentrega.gsl.order.domain.collection

import org.springframework.data.jpa.domain.Specification
import java.util.*
import javax.persistence.criteria.CriteriaBuilder
import javax.persistence.criteria.CriteriaQuery
import javax.persistence.criteria.Root

object PickupRequestSpecificationSet {
    fun uuid(uuid: UUID?) = Specification { root: Root<PickupRequest>,
                                            _: CriteriaQuery<*>?,
                                            criteriaBuilder: CriteriaBuilder ->
        criteriaBuilder.equal(root.get<Any>("uuid"), uuid)
    }

}