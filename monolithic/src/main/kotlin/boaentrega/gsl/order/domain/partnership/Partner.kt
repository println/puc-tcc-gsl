package boaentrega.gsl.order.domain.partnership

import boaentrega.gsl.order.configuration.constants.TableNames
import boaentrega.gsl.support.jpa.AuditableModel
import javax.persistence.Entity
import javax.persistence.Table

@Entity
@Table(name = TableNames.Domain.PARTNER)
class Partner : AuditableModel<Partner>() {
}