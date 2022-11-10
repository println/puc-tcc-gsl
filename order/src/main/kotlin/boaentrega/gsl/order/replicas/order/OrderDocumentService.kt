package boaentrega.gsl.order.replicas.order

import org.springframework.stereotype.Service
import java.util.*

@Service
class OrderDocumentService(private val repository: OrderDocumentRepository) {

    fun save(documentId: UUID, order: OrderDocument) {
        val optionalCustomer = findById(documentId)
        var entity = order
        if (optionalCustomer.isPresent) {
            entity = optionalCustomer.get()
            entity.name = order.name
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