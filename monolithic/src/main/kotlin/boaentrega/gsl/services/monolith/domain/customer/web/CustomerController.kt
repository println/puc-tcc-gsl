package boaentrega.gsl.services.monolith.domain.customer.web

import boaentrega.gsl.configuration.constants.ServiceNames
import boaentrega.gsl.services.monolith.domain.customer.Customer
import boaentrega.gsl.services.monolith.domain.customer.CustomerFilter
import boaentrega.gsl.services.monolith.domain.customer.CustomerService
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.web.bind.annotation.*
import java.util.*

@RequestMapping(ServiceNames.CUSTOMER)
@RestController
class CustomerController(val service: CustomerService) {

    @GetMapping
    fun getAll(
            @RequestParam(required = false) coordinates: String?,
            pageable: Pageable): Page<Customer> {
        val filter = CustomerFilter()
        return service.findAll(filter, pageable)
    }

    @GetMapping("/{id}")
    fun getById(
            @PathVariable("id") id: UUID): Customer {
        return service.findById(id)
    }
}