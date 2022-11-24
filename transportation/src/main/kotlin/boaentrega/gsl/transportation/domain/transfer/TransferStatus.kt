package boaentrega.gsl.transportation.domain.transfer

enum class TransferStatus {
    CREATED, MOVING, IN_STORAGE, END_OF_ROUTE;

    fun canChangeTo(status: TransferStatus) = when (this) {
        CREATED -> arrayOf(MOVING)
        MOVING -> arrayOf(
                IN_STORAGE,
                END_OF_ROUTE
        )
        IN_STORAGE -> arrayOf(MOVING)
        END_OF_ROUTE -> arrayOf()
    }.contains(status)
}
