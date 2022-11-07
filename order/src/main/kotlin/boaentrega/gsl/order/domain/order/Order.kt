package boaentrega.gsl.order.domain.order

import boaentrega.gsl.order.support.jpa.AuditableModel
import java.math.BigDecimal
import java.util.*
import javax.persistence.Entity
import javax.persistence.Table

@Entity
@Table(name = "orders")
data class Order(
        val customerId: UUID,
        val pickupAddress: String,
        val deliveryAddress: String,
        var value: BigDecimal? = null,
        var comment: String = "",
        var status: OrderStatus = OrderStatus.CREATED
) : AuditableModel<Order>()
