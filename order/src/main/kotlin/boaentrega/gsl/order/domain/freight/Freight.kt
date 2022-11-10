package boaentrega.gsl.order.domain.freight

import boaentrega.gsl.order.configuration.constants.TableNames
import boaentrega.gsl.order.support.jpa.AuditableModel
import javax.persistence.Entity
import javax.persistence.Table

@Entity
@Table(name = TableNames.Domain.FREIGHT)
class Freight() : AuditableModel<Freight>()