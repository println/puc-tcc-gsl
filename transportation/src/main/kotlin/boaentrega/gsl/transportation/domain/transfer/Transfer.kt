package boaentrega.gsl.transportation.domain.transfer

import boaentrega.gsl.transportation.configuration.constants.TableNames
import boaentrega.gsl.transportation.support.jpa.AuditableModel
import java.util.*
import javax.persistence.*

@Entity
@Table(name = TableNames.Domain.TRANSFER)
class Transfer(
        @Column(unique = true)
        val trackId: UUID,
        @Column(unique = true)
        val orderId: UUID,
        @Column(unique = true)
        val freightId: UUID,
        val deliveryAddress: String,
        var currentPosition: String,
        var nextStorage: String,
        var finalStorage: String,
        var partnerId: UUID? = null,
        @Enumerated(EnumType.STRING)
        var status: TransferStatus = TransferStatus.CREATED,
) : AuditableModel<Transfer>()