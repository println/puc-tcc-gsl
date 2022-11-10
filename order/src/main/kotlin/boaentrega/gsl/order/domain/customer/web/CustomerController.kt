package boaentrega.gsl.order.domain.customer.web

import boaentrega.gsl.order.configuration.constants.ServiceNames
import boaentrega.gsl.order.domain.customer.Customer
import boaentrega.gsl.order.domain.customer.CustomerFilter
import boaentrega.gsl.order.domain.customer.CustomerService
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