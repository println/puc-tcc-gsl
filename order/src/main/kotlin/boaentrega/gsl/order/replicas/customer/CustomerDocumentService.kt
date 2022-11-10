package boaentrega.gsl.order.replicas.customer

import org.springframework.stereotype.Service
import java.util.*

@Service
class CustomerDocumentService(private val repository: CustomerDocumentRepository) {

    fun save(documentId: UUID, document: CustomerDocument) {
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

    private fun findById(documentId: UUID): Optional<CustomerDocument> {
        return repository.findById(documentId)
    }
}