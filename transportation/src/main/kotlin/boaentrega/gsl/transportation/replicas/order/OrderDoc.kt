package boaentrega.gsl.transportation.replicas.order

import boaentrega.gsl.transportation.configuration.constants.TableNames
import boaentrega.gsl.transportation.support.jpa.Auditable
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
