package boaentrega.gsl.services.monolith.domain.order

enum class OrderStatus {
    WAITING_PAYMENT, REFUSED, ACCEPTED;

    fun canChangeTo(status: OrderStatus) = when (this) {
        WAITING_PAYMENT -> arrayOf(
                REFUSED,
                ACCEPTED
        )
        else -> arrayOf()
    }.contains(status)
}