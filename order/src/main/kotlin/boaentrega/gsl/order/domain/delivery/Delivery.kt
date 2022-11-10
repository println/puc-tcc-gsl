package boaentrega.gsl.order.domain.delivery

import boaentrega.gsl.order.configuration.constants.TableNames
import boaentrega.gsl.order.support.jpa.AuditableModel
import javax.persistence.Entity
import javax.persistence.Table

@Entity
@Table(name = TableNames.Domain.DELIVERY)
class Delivery() : AuditableModel<Delivery>()