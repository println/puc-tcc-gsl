package boaentrega.gsl.order.replicas.order

import org.springframework.stereotype.Service
import java.util.*

@Service
class OrderDocService(private val repository: OrderDocRepository) {

    fun save(documentId: UUID, document: OrderDoc) {
        val optionalDocument = findById(documentId)
        var entity = document
        if (optionalDocument.isPresent) {
            entity = optionalDocument.get()
            entity.name = document.name
        }
        repository.save(entity)
    }

    fun delete(documentId: UUID) {
        repository.deleteById(documentId)
    }

    private fun findById(documentId: UUID): Optional<OrderDoc> {
        return repository.findById(documentId)
    }
}