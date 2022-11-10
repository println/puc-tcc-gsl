package boaentrega.gsl.order.domain.freight

import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.data.jpa.domain.Specification
import org.springframework.http.HttpStatus
import org.springframework.stereotype.Service
import org.springframework.web.server.ResponseStatusException
import java.util.*

@Service
class FreightService(
        private val repository: FreightRepository) {

    fun findAll(filter: FreightFilter, pageable: Pageable): Page<Freight> {
        val specification: Specification<Freight> = Specification.where(null)
        return repository.findAll(specification, pageable)
    }

    fun findById(id: UUID): Freight {
        val entityOptional = repository.findById(id)
        if (entityOptional.isEmpty) {
            throw ResponseStatusException(HttpStatus.NOT_FOUND, "entity not found")
        }
        return entityOptional.get()
    }
}