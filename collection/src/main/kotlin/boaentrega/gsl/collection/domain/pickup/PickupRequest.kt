package boaentrega.gsl.collection.domain.pickup

import boaentrega.gsl.collection.configuration.constants.TableNames
import boaentrega.gsl.collection.support.jpa.AuditableModel
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