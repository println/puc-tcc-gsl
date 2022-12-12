package boaentrega.gsl.services.monolith.domain.order

import boaentrega.gsl.configuration.constants.TableNames
import boaentrega.gsl.support.jpa.AuditableModel
import java.math.BigDecimal
import java.util.*
import javax.persistence.Entity
import javax.persistence.EnumType
import javax.persistence.Enumerated
import javax.persistence.Table

@Entity
@Table(name = TableNames.Domain.ORDER)
data class Order(
        val customerId: UUID,
        val pickupAddress: String,
        val deliveryAddress: String,
        var value: BigDecimal? = null,
        var comment: String = "",
        @Enumerated(EnumType.STRING)
        var status: OrderStatus = OrderStatus.WAITING_PAYMENT
) : AuditableModel<Order>()
