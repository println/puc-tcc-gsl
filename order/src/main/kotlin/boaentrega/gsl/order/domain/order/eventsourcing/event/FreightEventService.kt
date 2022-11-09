package boaentrega.gsl.order.domain.order.eventsourcing.event

import boaentrega.gsl.order.configuration.constants.EventSourcingBeansConstants
import boaentrega.gsl.order.support.eventsourcing.connectors.DedicatedProducerConnector
import boaentrega.gsl.order.support.eventsourcing.messages.EventMessage
import boaentrega.gsl.order.support.extensions.ClassExtensions.logger
import boaentrega.gsl.order.support.extensions.ClassExtensions.toJsonString
import gsl.schemas.FreightEvent
import gsl.schemas.FreightEventStatus
import org.springframework.beans.factory.annotation.Qualifier
import org.springframework.stereotype.Service
import java.time.LocalDate
import java.util.*

@Service
class FreightEventService(
        @Qualifier(EventSourcingBeansConstants.FREIGHT_EVENT_PRODUCER)
        private val dedicatedProducerConnector: DedicatedProducerConnector) {

    private val logger = logger()

    companion object {
        const val ORDER_SERVICE = "order"
        const val FREIGHT_SERVICE = "freight"
        const val COLLECTOR_SERVICE = "collector"
        const val DELIVERY_SERVICE = "delivery"
    }

    fun notifyCreated(trackId: UUID, source: String, description: String) {
        val status = FreightEventStatus.CREATED
        notify(trackId, status, source, description)
    }

    fun notifyCanceling(trackId: UUID, source: String, description: String) {
        val status = FreightEventStatus.CANCELING
        notify(trackId, status, source, description)
    }

    fun notifyCanceled(trackId: UUID, source: String, description: String) {
        val status = FreightEventStatus.CANCELED
        notify(trackId, status, source, description)
    }

    fun notifyFinished(trackId: UUID, source: String, description: String) {
        val status = FreightEventStatus.FINISHED
        notify(trackId, status, source, description)
    }

    fun notifyCollectionStarted(trackId: UUID, source: String, description: String) {
        val status = FreightEventStatus.COLLECTION_STARTED
        notify(trackId, status, source, description)
    }

    fun notifyCollectionPickupOut(trackId: UUID, source: String, description: String) {
        val status = FreightEventStatus.COLLECTION_PICKUP_OUT
        notify(trackId, status, source, description)
    }

    fun notifyCollectionPickupTaken(trackId: UUID, source: String, description: String) {
        val status = FreightEventStatus.COLLECTION_PICKUP_TAKEN
        notify(trackId, status, source, description)
    }

    fun notifyCollectionPackagePreparing(trackId: UUID, source: String, description: String) {
        val status = FreightEventStatus.COLLECTION_PACKAGE_PREPARING
        notify(trackId, status, source, description)
    }

    fun notifyCollectionPackageReadyToMove(trackId: UUID, source: String, description: String) {
        val status = FreightEventStatus.COLLECTION_PACKAGE_READY_TO_MOVE
        notify(trackId, status, source, description)
    }

    fun notifyPackageMoveStarted(trackId: UUID, source: String, description: String) {
        val status = FreightEventStatus.PACKAGE_MOVE_STARTED
        notify(trackId, status, source, description)
    }

    fun notifyPackageMovingOnToNextStorage(trackId: UUID, source: String, description: String) {
        val status = FreightEventStatus.PACKAGE_MOVING_ON_TO_NEXT_STORAGE
        notify(trackId, status, source, description)
    }

    fun notifyPackageReceivedByStorage(trackId: UUID, source: String, description: String) {
        val status = FreightEventStatus.PACKAGE_RECEIVED_BY_STORAGE
        notify(trackId, status, source, description)
    }

    fun notifyPackageReachedFinalStorage(trackId: UUID, source: String, description: String) {
        val status = FreightEventStatus.PACKAGE_REACHED_FINAL_STORAGE
        notify(trackId, status, source, description)
    }

    fun notifyPackageOutForDelivery(trackId: UUID, source: String, description: String) {
        val status = FreightEventStatus.PACKAGE_OUT_FOR_DELIVERY
        notify(trackId, status, source, description)
    }

    fun notifyPackageDelivered(trackId: UUID, source: String, description: String) {
        val status = FreightEventStatus.PACKAGE_DELIVERED
        notify(trackId, status, source, description)
    }

    fun notifyPackageDeliveryFailed(trackId: UUID, source: String, description: String) {
        val status = FreightEventStatus.PACKAGE_DELIVERY_FAILED
        notify(trackId, status, source, description)
    }

    fun notifyPackageDeliveryProcessReset(trackId: UUID, source: String, description: String) {
        val status = FreightEventStatus.PACKAGE_DELIVERY_PROCESS_RESET
        notify(trackId, status, source, description)
    }

    private fun notify(trackId: UUID, status: FreightEventStatus, source: String, description: String) {
        val payload = FreightEvent(trackId, status, source, description, LocalDate.now())
        val message = EventMessage(trackId, payload)
        dedicatedProducerConnector.publish(message)
        logger.info("Event has been emitted to [${dedicatedProducerConnector.getId()}]: ${message.toJsonString()}")
    }
}