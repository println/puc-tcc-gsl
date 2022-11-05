package boaentrega.gsl.order.domain.order

import boaentrega.gsl.order.support.jpa.AuditableModel
import javax.persistence.Entity
import javax.persistence.Table

@Entity
@Table(name = "orders")
class Order : AuditableModel<Order>() {
    val name = "test"

}
