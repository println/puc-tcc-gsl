package boaentrega.gsl.order.replicas.freight

import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository
import java.util.*

@Repository
interface FreightDocumentRepository : JpaRepository<FreightDocument, UUID> {
}