package boaentrega.gsl.order.domain.delivery

enum class DeliveryStatus {
    CREATED, OUT_FOR_DELIVERY, FAILED_DELIVERY_ATTEMPT, SUCCESSFULLY_DELIVERED, RETRY_DELIVERY
}
