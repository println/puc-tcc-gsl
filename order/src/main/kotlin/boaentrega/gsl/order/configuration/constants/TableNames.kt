package boaentrega.gsl.order.configuration.constants

object TableNames {
    object Domain {
        const val PARTNER = "partners"
        const val ORDER = "orders"
        const val FREIGHT = "freights"
        const val DELIVERY = "deliveries"
        const val CUSTOMER = "customers"
        const val PICKUP_REQUEST = "pickup_requests"
        const val MOVEMENT = "movements"
    }

    object Replica {
        const val CUSTOMER = "customer_docs"
        const val ORDER = "order_docs"
        const val FREIGHT = "freight_docs"
    }
}