package boaentrega.gsl.freight.domain.freight

enum class FreightStatus() {

    CREATED,
    COLLECTION_STARTED,
    COLLECTION_PICKUP_OUT,
    COLLECTION_PICKUP_TAKEN,
    COLLECTION_PACKAGE_PREPARING,
    COLLECTION_PACKAGE_READY_TO_MOVE,
    IN_TRANSIT_PACKAGE_STARTED,
    IN_TRANSIT_PACKAGE_MOVING_ON_TO_NEXT_STORAGE,
    IN_TRANSIT_PACKAGE_RECEIVED_BY_STORAGE,
    IN_TRANSIT_PACKAGE_REACHED_FINAL_STORAGE,
    DELIVERY_STARTED,
    DELIVERY_OUT_FOR,
    DELIVERY_FAILED,
    DELIVERY_PROCESS_RESTART,
    DELIVERY_SUCCESS,
    CANCELING,
    CANCELED,
    FINISHED;

    fun canChangeTo(status: FreightStatus) = when (this) {
        FINISHED -> arrayOf()
        CANCELED -> arrayOf()
        CANCELING -> arrayOf(
                CANCELED
        )
        DELIVERY_SUCCESS -> arrayOf(
                FINISHED,
                CANCELING
        )
        DELIVERY_PROCESS_RESTART -> arrayOf(
                DELIVERY_OUT_FOR,
                CANCELING
        )
        DELIVERY_FAILED -> arrayOf(
                DELIVERY_PROCESS_RESTART,
                CANCELING
        )
        DELIVERY_OUT_FOR -> arrayOf(
                DELIVERY_FAILED,
                DELIVERY_SUCCESS,
                CANCELING
        )
        DELIVERY_STARTED -> arrayOf(
                DELIVERY_OUT_FOR,
                CANCELING
        )
        IN_TRANSIT_PACKAGE_REACHED_FINAL_STORAGE -> arrayOf(
                DELIVERY_STARTED,
                CANCELING
        )
        IN_TRANSIT_PACKAGE_RECEIVED_BY_STORAGE -> arrayOf(
                IN_TRANSIT_PACKAGE_REACHED_FINAL_STORAGE,
                IN_TRANSIT_PACKAGE_MOVING_ON_TO_NEXT_STORAGE,
                CANCELING
        )
        IN_TRANSIT_PACKAGE_MOVING_ON_TO_NEXT_STORAGE -> arrayOf(
                IN_TRANSIT_PACKAGE_RECEIVED_BY_STORAGE,
                CANCELING
        )
        IN_TRANSIT_PACKAGE_STARTED -> arrayOf(
                IN_TRANSIT_PACKAGE_MOVING_ON_TO_NEXT_STORAGE,
                CANCELING
        )
        COLLECTION_PACKAGE_READY_TO_MOVE -> arrayOf(
                IN_TRANSIT_PACKAGE_STARTED,
                CANCELING
        )
        COLLECTION_PACKAGE_PREPARING -> arrayOf(
                COLLECTION_PACKAGE_READY_TO_MOVE,
                CANCELING
        )
        COLLECTION_PICKUP_TAKEN -> arrayOf(
                COLLECTION_PACKAGE_PREPARING,
                CANCELING
        )
        COLLECTION_PICKUP_OUT -> arrayOf(
                COLLECTION_PICKUP_TAKEN,
                CANCELING
        )
        COLLECTION_STARTED -> arrayOf(
                COLLECTION_PICKUP_OUT,
                CANCELING
        )
        CREATED -> arrayOf(
                COLLECTION_STARTED,
                CANCELING
        )
    }.contains(status)
}
