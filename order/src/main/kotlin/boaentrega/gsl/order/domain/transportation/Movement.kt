package boaentrega.gsl.order.domain.transportation

import boaentrega.gsl.order.configuration.constants.TableNames
import boaentrega.gsl.order.support.jpa.AuditableModel
import javax.persistence.Entity
import javax.persistence.Table

@Entity
@Table(name = TableNames.Domain.MOVEMENT)
class Movement() : AuditableModel<Movement>()