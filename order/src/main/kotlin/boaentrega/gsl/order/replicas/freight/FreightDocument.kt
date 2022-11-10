package boaentrega.gsl.order.replicas.freight

import boaentrega.gsl.order.configuration.constants.TableNames
import boaentrega.gsl.order.support.jpa.Auditable
import java.util.*
import javax.persistence.Entity
import javax.persistence.Id
import javax.persistence.Table

@Entity
@Table(name = TableNames.Replica.FREIGHT)
data class FreightDocument(
        @Id
        val id: UUID,
        var name: String
) : Auditable<FreightDocument>()
