package boaentrega.gsl.delivery.domain.delivery

enum class DeliveryStatus {
    CREATED, OUT_FOR_DELIVERY, FAILED_DELIVERY_ATTEMPT, RETRY_DELIVERY, SUCCESSFULLY_DELIVERED;

    fun canChangeTo(status: DeliveryStatus) = when (this) {
        CREATED -> arrayOf(OUT_FOR_DELIVERY)
        OUT_FOR_DELIVERY -> arrayOf(
                FAILED_DELIVERY_ATTEMPT,
                SUCCESSFULLY_DELIVERED
        )
        FAILED_DELIVERY_ATTEMPT -> arrayOf(RETRY_DELIVERY)
        RETRY_DELIVERY -> arrayOf(OUT_FOR_DELIVERY)
        SUCCESSFULLY_DELIVERED -> arrayOf()
    }.contains(status)
}
