package boaentrega.gsl.collection.replicas.freight

import boaentrega.gsl.collection.configuration.constants.TableNames
import boaentrega.gsl.collection.support.jpa.Auditable
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
