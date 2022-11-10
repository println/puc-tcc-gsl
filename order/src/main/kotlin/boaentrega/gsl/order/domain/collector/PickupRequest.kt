package boaentrega.gsl.order.domain.collector

import boaentrega.gsl.order.support.jpa.AuditableModel
import java.util.*
import javax.persistence.Column
import javax.persistence.Entity
import javax.persistence.Table

@Entity
@Table(name = "pickup_requests")
data class PickupRequest(
        @Column(unique = true)
        val trackId: UUID,
        @Column(unique = true)
        val orderId: UUID,
        @Column(unique = true)
        val freightId: UUID,
        val pickupAddress: String,
        val destination: String,
        var status: PickupRequestStatus = PickupRequestStatus.WAITING,
        var collectorEmployee: String? = null,
        var packerEmployee: String? = null,
        var packageAddress: String? =  null
) : AuditableModel<PickupRequest>()