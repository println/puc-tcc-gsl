package boaentrega.gsl.transportation.replicas.freight

import org.springframework.stereotype.Service
import java.util.*

@Service
class FreightDocService(private val repository: FreightDocRepository) {

    fun save(documentId: UUID, document: FreightDoc) {
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

    private fun findById(documentId: UUID): Optional<FreightDoc> {
        return repository.findById(documentId)
    }
}