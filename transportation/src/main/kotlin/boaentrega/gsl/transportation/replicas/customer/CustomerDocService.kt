package boaentrega.gsl.transportation.replicas.customer

import org.springframework.stereotype.Service
import java.util.*

@Service
class CustomerDocService(private val repository: CustomerDocRepository) {

    fun save(documentId: UUID, document: CustomerDoc) {
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

    private fun findById(documentId: UUID): Optional<CustomerDoc> {
        return repository.findById(documentId)
    }
}