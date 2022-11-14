package boaentrega.gsl.order.replicas.order

import boaentrega.gsl.order.configuration.constants.TableNames
import boaentrega.gsl.order.support.jpa.Auditable
import java.util.*
import javax.persistence.Entity
import javax.persistence.Id
import javax.persistence.Table

@Entity
@Table(name = TableNames.Replica.ORDER)
data class OrderDoc(
        @Id
        val id: UUID,
        var name: String
) : Auditable<OrderDoc>()
