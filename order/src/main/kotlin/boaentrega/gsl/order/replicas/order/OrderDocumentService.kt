package boaentrega.gsl.order.replicas.order

import org.springframework.stereotype.Service
import java.util.*

@Service
class OrderDocumentService(private val repository: OrderDocumentRepository) {

    fun save(documentId: UUID, document: OrderDocument) {
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

    private fun findById(documentId: UUID): Optional<OrderDocument> {
        return repository.findById(documentId)
    }
}