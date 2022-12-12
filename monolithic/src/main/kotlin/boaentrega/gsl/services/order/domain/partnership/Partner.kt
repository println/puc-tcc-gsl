package boaentrega.gsl.services.order.domain.partnership

import boaentrega.gsl.configuration.constants.TableNames
import boaentrega.gsl.support.jpa.AuditableModel
import javax.persistence.Entity
import javax.persistence.Table

@Entity
@Table(name = TableNames.Domain.PARTNER)
class Partner : AuditableModel<Partner>() {
}