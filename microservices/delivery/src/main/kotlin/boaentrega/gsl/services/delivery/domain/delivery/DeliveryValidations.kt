package boaentrega.gsl.services.delivery.domain.delivery

import boaentrega.gsl.services.delivery.domain.delivery.DeliveryStatus.*
import java.time.LocalTime


object DeliveryValidations {


    fun canSchedule(entity: Delivery?, time: LocalTime) =
            (entity != null)
                    .and(isValidTime(time))
                    .and(arrayOf(CREATED, RETRY_DELIVERY, FAILED_DELIVERY_ATTEMPT).contains(entity!!.status))

    fun canDelivery(entity: Delivery?) =
            (entity != null)
                    .and(entity!!.let { it.currentPosition == it.storageAddress })
                    .and(entity.status.canChangeTo(OUT_FOR_DELIVERY))

    fun canReturnPackageToRetry(entity: Delivery?) =
            (entity != null)
                    .and(entity!!.let { it.currentPosition == "${it.deliveryAddress}-${it.storageAddress}" })
                    .and(entity.status.canChangeTo(RETRY_DELIVERY))

    fun isSuccessful(entity: Delivery?) =
            (entity != null)
                    .and(entity!!.let { it.currentPosition == "${it.storageAddress}-${it.deliveryAddress}" })
                    .and(entity.status.canChangeTo(SUCCESSFULLY_DELIVERED))

    fun canFailed(entity: Delivery?) =
            (entity != null)
                    .and(entity!!.let { it.currentPosition == "${it.storageAddress}-${it.deliveryAddress}" })
                    .and(entity.status.canChangeTo(FAILED_DELIVERY_ATTEMPT))

    private fun isValidTime(time: LocalTime): Boolean {
        val start = LocalTime.parse("10:00:00")
        val stop = LocalTime.parse("18:00:00")
        return !time.isBefore(start) && !time.isAfter(stop)
    }

}