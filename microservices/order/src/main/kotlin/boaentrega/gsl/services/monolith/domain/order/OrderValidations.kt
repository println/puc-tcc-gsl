package boaentrega.gsl.services.monolith.domain.order

import boaentrega.gsl.services.monolith.domain.order.OrderStatus.ACCEPTED
import boaentrega.gsl.services.monolith.domain.order.OrderStatus.REFUSED
import java.math.BigDecimal


object OrderValidations {
    fun canApprove(value: BigDecimal, entity: Order?) =
            (entity != null)
                    .and(value > BigDecimal.ZERO)
                    .and(entity!!.status.canChangeTo(ACCEPTED))


    fun canRefuse(reason: String, entity: Order?) =
            (entity != null)
                    .and(reason.isNotBlank())
                    .and(entity!!.status.canChangeTo(REFUSED))
}