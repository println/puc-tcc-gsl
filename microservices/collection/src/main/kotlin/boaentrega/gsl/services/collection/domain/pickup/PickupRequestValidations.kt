package boaentrega.gsl.services.collection.domain.pickup


import boaentrega.gsl.services.collection.domain.pickup.PickupRequestStatus.*


object PickupRequestValidations {
    fun canPickup(entity: PickupRequest?, collectorEmployee: String) =
            (entity != null)
                    .and(collectorEmployee.isNotBlank())
                    .and(entity!!.status.canChangeTo(PICKUP_PROCESS))

    fun canTaken(entity: PickupRequest?, packageAddress: String) =
            (entity != null)
                    .and(packageAddress.isNotBlank())
                    .and(entity!!.status.canChangeTo(TAKEN))

    fun canPackaging(entity: PickupRequest?, packerEmployee: String) =
            (entity != null)
                    .and(packerEmployee.isNotBlank())
                    .and(entity!!.status.canChangeTo(ON_PACKAGING))

    fun isReady(entity: PickupRequest?, dispenserAddress: String) =
            (entity != null)
                    .and(dispenserAddress.isNotBlank())
                    .and(entity!!.status.canChangeTo(FINISHED))
}