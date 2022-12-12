package boaentrega.gsl.services.monolith.domain.partnership

import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.data.jpa.domain.Specification
import org.springframework.http.HttpStatus
import org.springframework.stereotype.Service
import org.springframework.web.server.ResponseStatusException
import java.util.*

@Service
class PartnerService(
        private val repository: PartnerRepository) {

    fun findAll(filter: PartnerFilter, pageable: Pageable): Page<Partner> {
        val specification: Specification<Partner> = Specification.where(null)
        return repository.findAll(specification, pageable)
    }

    fun findById(id: UUID): Partner {
        val entityOptional = repository.findById(id)
        if (entityOptional.isEmpty) {
            throw ResponseStatusException(HttpStatus.NOT_FOUND, "entity not found")
        }
        return entityOptional.get()
    }
}