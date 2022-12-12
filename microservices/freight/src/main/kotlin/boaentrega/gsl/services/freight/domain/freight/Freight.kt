package boaentrega.gsl.services.freight.domain.freight

import boaentrega.gsl.configuration.constants.TableNames
import boaentrega.gsl.support.jpa.AuditableModel
import java.time.Instant
import java.util.*
import javax.persistence.*

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
        @Enumerated(EnumType.STRING)
        var status: FreightStatus = FreightStatus.CREATED,
        var lastUpdated: Instant = Instant.now()
) : AuditableModel<Freight>()
