package boaentrega.gsl.services.freight.replicas.order

import boaentrega.gsl.configuration.constants.TableNames
import boaentrega.gsl.support.jpa.Auditable
import java.math.BigDecimal
import java.util.*
import javax.persistence.Entity
import javax.persistence.Id
import javax.persistence.Table

@Entity
@Table(name = TableNames.Replica.ORDER)
data class OrderDoc(
        @Id
        val id: UUID,
        val customerId: UUID,
        val pickupAddress: String,
        val deliveryAddress: String,
        var value: BigDecimal? = null,
        var comment: String? = null,
        var status: String
) : Auditable<OrderDoc>()
