package boaentrega.gsl.order.replicas.customer

import org.springframework.stereotype.Service
import java.util.*

@Service
class CustomerDocumentService(private val repository: CustomerDocumentRepository) {

    fun save(documentId: UUID, customer: CustomerDocument) {
        val optionalCustomer = findById(documentId)
        var entity = customer
        if (optionalCustomer.isPresent) {
            entity = optionalCustomer.get()
            entity.name = customer.name
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