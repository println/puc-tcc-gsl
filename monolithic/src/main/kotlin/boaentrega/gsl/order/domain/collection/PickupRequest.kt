package boaentrega.gsl.order.domain.collection

import boaentrega.gsl.order.configuration.constants.TableNames
import boaentrega.gsl.support.jpa.AuditableModel
import java.util.*
import javax.persistence.*

@Entity
@Table(name = TableNames.Domain.PICKUP_REQUEST)
data class PickupRequest(
        @Column(unique = true)
        val trackId: UUID,
        @Column(unique = true)
        val orderId: UUID,
        @Column(unique = true)
        val freightId: UUID,
        val senderAddress: String,
        val deliveryAddress: String,
        @Enumerated(EnumType.STRING)
        var status: PickupRequestStatus = PickupRequestStatus.WAITING,
        var collectorEmployee: String? = null,
        var packerEmployee: String? = null,
        var currentPosition: String? = null
) : AuditableModel<PickupRequest>()