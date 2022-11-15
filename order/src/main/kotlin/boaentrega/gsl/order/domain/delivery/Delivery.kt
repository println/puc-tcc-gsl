package boaentrega.gsl.order.domain.delivery

import boaentrega.gsl.order.configuration.constants.TableNames
import boaentrega.gsl.order.support.jpa.AuditableModel
import com.fasterxml.jackson.annotation.JsonFormat
import java.time.LocalTime
import java.util.*
import javax.persistence.Column
import javax.persistence.Entity
import javax.persistence.Table

@Entity
@Table(name = TableNames.Domain.DELIVERY)
data class Delivery(
        @Column(unique = true)
        val trackId: UUID,
        @Column(unique = true)
        val orderId: UUID,
        @Column(unique = true)
        val freightId: UUID,
        val storageAddress: String,
        val deliveryAddress: String,
        var currentPosition: String,
        var partnerId: UUID? = null,
        var status: DeliveryStatus = DeliveryStatus.CREATED,
        @Column(columnDefinition = "TIME")
        @JsonFormat(pattern="HH:mm")
        var preferredDeliveryTime: LocalTime? = null
) : AuditableModel<Delivery>()