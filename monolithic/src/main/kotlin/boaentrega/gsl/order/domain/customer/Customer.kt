package boaentrega.gsl.order.domain.customer

import boaentrega.gsl.order.configuration.constants.TableNames
import boaentrega.gsl.support.jpa.AuditableModel
import javax.persistence.Entity
import javax.persistence.Table

@Entity
@Table(name = TableNames.Domain.CUSTOMER)
class Customer : AuditableModel<Customer>() {
}