package boaentrega.gsl.order.domain.transportation

import boaentrega.gsl.order.configuration.constants.TableNames
import boaentrega.gsl.order.support.jpa.AuditableModel
import java.util.*
import javax.persistence.Column
import javax.persistence.Entity
import javax.persistence.Table

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
        var status: TransferStatus = TransferStatus.CREATED,
) : AuditableModel<Transfer>()