package boaentrega.gsl.order.domain.delivery

import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.data.jpa.domain.Specification
import org.springframework.http.HttpStatus
import org.springframework.stereotype.Service
import org.springframework.web.server.ResponseStatusException
import java.util.*

@Service
class DeliveryService(
        private val repository: DeliveryRepository) {

    fun findAll(filter: DeliveryFilter, pageable: Pageable): Page<Delivery> {
        val specification: Specification<Delivery> = Specification.where(null)
        return repository.findAll(specification, pageable)
    }

    fun findById(id: UUID): Delivery {
        val entityOptional = repository.findById(id)
        if (entityOptional.isEmpty) {
            throw ResponseStatusException(HttpStatus.NOT_FOUND, "entity not found")
        }
        return entityOptional.get()
    }
}