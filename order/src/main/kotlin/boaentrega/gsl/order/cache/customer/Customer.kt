package boaentrega.gsl.order.cache.customer

import java.util.*
import javax.persistence.Entity
import javax.persistence.Id

@Entity
data class Customer(@Id val id: UUID)
