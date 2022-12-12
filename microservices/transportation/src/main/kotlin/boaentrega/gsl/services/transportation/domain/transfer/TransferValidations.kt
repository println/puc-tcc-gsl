package boaentrega.gsl.services.transportation.domain.transfer


import boaentrega.gsl.services.transportation.domain.transfer.TransferStatus.*


object TransferValidations {
    fun canMove(entity: Transfer?, collectorEmployee: String) =
            (entity != null)
                    .and(collectorEmployee.isNotBlank())
                    .and(entity!!.status.canChangeTo(MOVING))

    fun canReceive(entity: Transfer?) =
            (entity != null)
                    .and(entity!!.status.canChangeTo(IN_STORAGE))
                    .and(hasNextStorage(entity))

    fun canTerminate(entity: Transfer?) =
            (entity != null)
                    .and(entity!!.status.canChangeTo(END_OF_ROUTE))
                    .and(!hasNextStorage(entity))

    private fun hasNextStorage(entity: Transfer) =
            entity.let { it.nextStorage != it.finalStorage }

}