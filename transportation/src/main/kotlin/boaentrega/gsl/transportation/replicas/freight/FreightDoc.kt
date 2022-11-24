package boaentrega.gsl.transportation.replicas.freight

import boaentrega.gsl.transportation.configuration.constants.TableNames
import boaentrega.gsl.transportation.support.jpa.Auditable
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
