package boaentrega.gsl.order.replicas.customer

import boaentrega.gsl.order.support.jpa.Auditable
import java.util.*
import javax.persistence.Entity
import javax.persistence.Id
import javax.persistence.Table

@Entity
@Table(name = "document_customers")
data class CustomerDocument(
        @Id
        val id: UUID,
        var name: String
) : Auditable<CustomerDocument>()
