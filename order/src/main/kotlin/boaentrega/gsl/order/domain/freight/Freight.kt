package boaentrega.gsl.order.domain.freight

import boaentrega.gsl.order.configuration.constants.TableNames
import boaentrega.gsl.order.support.jpa.AuditableModel
import java.time.Instant
import java.util.*
import javax.persistence.Column
import javax.persistence.Entity
import javax.persistence.Table

@Entity
@Table(name = TableNames.Domain.FREIGHT)
data class Freight(
        @Column(unique = true)
        val trackId: UUID,
        @Column(unique = true)
        val orderId: UUID,
        val senderAddress: String,
        val deliveryAddress: String,
        var currentPosition: String,
        var status: FreightStatus = FreightStatus.CREATED,
        var lastUpdated: Instant = Instant.now()
) : AuditableModel<Freight>()
