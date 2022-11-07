package boaentrega.gsl.order.domain.collector

import boaentrega.gsl.order.support.jpa.AuditableModel
import java.util.*
import javax.persistence.Entity
import javax.persistence.Table

@Entity
@Table(name = "pickup_requests")
data class PickupRequest(
        val trackId: UUID,
        val orderId: UUID,
        val freightId: UUID,
        val pickupAddress: String,
        val destination: String,
        var status: PickupRequestStatus = PickupRequestStatus.WAITING,
        var collectorEmployee: String? = null,
        var packerEmployee: String? = null,
        var packageAddress: String? =  null
) : AuditableModel<PickupRequest>()