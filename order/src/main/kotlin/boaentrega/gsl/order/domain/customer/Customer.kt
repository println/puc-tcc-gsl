package boaentrega.gsl.order.domain.customer

import boaentrega.gsl.order.support.jpa.AuditableModel
import javax.persistence.Entity
import javax.persistence.Table

@Entity
@Table(name ="customers")
class Customer: AuditableModel<Customer>() {
}