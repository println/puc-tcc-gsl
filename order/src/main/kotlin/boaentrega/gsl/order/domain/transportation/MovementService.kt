package boaentrega.gsl.order.domain.transportation

import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.data.jpa.domain.Specification
import org.springframework.http.HttpStatus
import org.springframework.stereotype.Service
import org.springframework.web.server.ResponseStatusException
import java.util.*

@Service
class MovementService(
        private val repository: MovementRepository) {

    fun findAll(filter: MovementFilter, pageable: Pageable): Page<Movement> {
        val specification: Specification<Movement> = Specification.where(null)
        return repository.findAll(specification, pageable)
    }

    fun findById(id: UUID): Movement {
        val entityOptional = repository.findById(id)
        if (entityOptional.isEmpty) {
            throw ResponseStatusException(HttpStatus.NOT_FOUND, "entity not found")
        }
        return entityOptional.get()
    }
}