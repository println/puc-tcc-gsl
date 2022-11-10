package boaentrega.gsl.order.domain.order.web

import java.util.*

data class OrderDto(
        val customerId: UUID,
        val pickupAddress: String,
        val deliveryAddress: String
)
