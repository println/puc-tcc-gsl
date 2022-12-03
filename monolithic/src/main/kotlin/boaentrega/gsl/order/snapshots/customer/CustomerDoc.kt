package boaentrega.gsl.order.snapshots.customer

import boaentrega.gsl.order.configuration.constants.TableNames
import boaentrega.gsl.support.jpa.Auditable
import java.util.*
import javax.persistence.Entity
import javax.persistence.Id
import javax.persistence.Table

@Entity
@Table(name = TableNames.Replica.CUSTOMER)
data class CustomerDoc(
        @Id
        val id: UUID,
        var name: String
) : Auditable<CustomerDoc>()
