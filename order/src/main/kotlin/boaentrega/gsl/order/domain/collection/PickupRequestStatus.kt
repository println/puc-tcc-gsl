package boaentrega.gsl.order.domain.collection

enum class PickupRequestStatus {
    WAITING, PICKUP_PROCESS, TAKEN, ON_PACKAGING, FINISHED;

    fun canChangeTo(status: PickupRequestStatus) = when (this) {
        WAITING -> arrayOf(PICKUP_PROCESS)
        PICKUP_PROCESS -> arrayOf(TAKEN)
        TAKEN -> arrayOf(ON_PACKAGING)
        ON_PACKAGING -> arrayOf(FINISHED)
        FINISHED -> arrayOf()
    }.contains(status)
}