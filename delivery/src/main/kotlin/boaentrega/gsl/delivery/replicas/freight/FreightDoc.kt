package boaentrega.gsl.delivery.replicas.freight

import boaentrega.gsl.delivery.configuration.constants.TableNames
import boaentrega.gsl.delivery.support.jpa.Auditable
import java.util.*
import javax.persistence.Entity
import javax.persistence.Id
import javax.persistence.Table

@Entity
@Table(name = TableNames.Replica.FREIGHT)
data class FreightDoc(
        @Id
        val id: UUID,
        var name: String
) : Auditable<FreightDoc>()
