package boaentrega.gsl.services.monolith.domain.customer

import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.data.jpa.domain.Specification
import org.springframework.http.HttpStatus
import org.springframework.stereotype.Service
import org.springframework.web.server.ResponseStatusException
import java.util.*

@Service
class CustomerService(
        private val repository: CustomerRepository) {

    fun findAll(filter: CustomerFilter, pageable: Pageable): Page<Customer> {
        val specification: Specification<Customer> = Specification.where(null)
        return repository.findAll(specification, pageable)
    }

    fun findById(id: UUID): Customer {
        val entityOptional = repository.findById(id)
        if (entityOptional.isEmpty) {
            throw ResponseStatusException(HttpStatus.NOT_FOUND, "entity not found")
        }
        return entityOptional.get()
    }
}