package boaentrega.gsl.services.order.domain.customer

import boaentrega.gsl.configuration.constants.TableNames
import boaentrega.gsl.support.jpa.AuditableModel
import javax.persistence.Entity
import javax.persistence.Table

@Entity
@Table(name = TableNames.Domain.CUSTOMER)
class Customer : AuditableModel<Customer>() {
}