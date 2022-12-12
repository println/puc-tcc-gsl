package boaentrega.gsl.services.freight.replicas.order

import org.springframework.stereotype.Service
import java.util.*

@Service
class OrderDocService(private val repository: OrderDocRepository) {

    fun save(documentId: UUID, document: OrderDoc) {
        val optionalDocument = findById(documentId)
        var entity = document
        if (optionalDocument.isPresent) {
            entity = optionalDocument.get()
            entity.value = document.value
            entity.comment = document.comment
            entity.status = document.status
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