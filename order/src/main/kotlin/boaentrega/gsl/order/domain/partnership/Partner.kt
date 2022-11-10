package boaentrega.gsl.order.domain.partnership

import boaentrega.gsl.order.configuration.constants.TableNames
import boaentrega.gsl.order.support.jpa.AuditableModel
import javax.persistence.Entity
import javax.persistence.Table

@Entity
@Table(name = TableNames.Domain.PARTNER)
class Partner : AuditableModel<Partner>() {
}