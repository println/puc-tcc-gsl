package boaentrega.gsl.delivery.replicas.customer

import boaentrega.gsl.delivery.configuration.constants.TableNames
import boaentrega.gsl.delivery.support.jpa.Auditable
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
