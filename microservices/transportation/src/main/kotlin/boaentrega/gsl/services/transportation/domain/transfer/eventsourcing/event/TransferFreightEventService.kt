package boaentrega.gsl.services.transportation.domain.transfer.eventsourcing.event

import boaentrega.gsl.configuration.constants.EventSourcingBeanQualifiers
import boaentrega.gsl.support.eventsourcing.connectors.DedicatedProducerConnector
import boaentrega.gsl.support.eventsourcing.messages.EventMessage
import boaentrega.gsl.support.extensions.ClassExtensions.logger
import boaentrega.gsl.support.extensions.ClassExtensions.toJsonString
import gsl.schemas.FreightEvent
import gsl.schemas.FreightEventStatus
import org.springframework.beans.factory.annotation.Qualifier
import org.springframework.stereotype.Service
import java.time.Instant
import java.util.*

@Service
class TransferFreightEventService(
        @Qualifier(EventSourcingBeanQualifiers.FREIGHT_EVENT_PRODUCER)
        private val dedicatedProducerConnector: DedicatedProducerConnector) {

    private val logger = logger()

    fun notifyCreated(trackId: UUID, freightId: UUID, source: String,
                      currentPosition: String, description: String, lastUpdated: Instant) {
        val status = FreightEventStatus.CREATED
        notify(trackId, freightId, status, source, currentPosition, description, lastUpdated)
    }

    fun notifyCanceling(trackId: UUID, freightId: UUID, source: String, currentPosition: String, description: String) {
        val status = FreightEventStatus.CANCELING
        notify(trackId, freightId, status, source, currentPosition, description)
    }

    fun notifyCanceled(trackId: UUID, freightId: UUID, source: String,
                       currentPosition: String, description: String) {
        val status = FreightEventStatus.CANCELED
        notify(trackId, freightId, status, source, currentPosition, description)
    }

    fun notifyFinished(trackId: UUID, freightId: UUID, source: String, currentPosition: String,
                       description: String, lastUpdated: Instant) {
        val status = FreightEventStatus.FINISHED
        notify(trackId, freightId, status, source, currentPosition, description, lastUpdated)
    }

    fun notifyCollectionStarted(trackId: UUID, freightId: UUID, source: String,
                                currentPosition: String, description: String) {
        val status = FreightEventStatus.COLLECTION_STARTED
        notify(trackId, freightId, status, source, currentPosition, description)
    }

    fun notifyCollectionPickupOut(trackId: UUID, freightId: UUID, source: String,
                                  currentPosition: String, description: String) {
        val status = FreightEventStatus.COLLECTION_PICKUP_OUT
        notify(trackId, freightId, status, source, currentPosition, description)
    }

    fun notifyCollectionPickupTaken(trackId: UUID, freightId: UUID, source: String,
                                    currentPosition: String, description: String) {
        val status = FreightEventStatus.COLLECTION_PICKUP_TAKEN
        notify(trackId, freightId, status, source, currentPosition, description)
    }

    fun notifyCollectionPackagePreparing(trackId: UUID, freightId: UUID, source: String,
                                         currentPosition: String, description: String) {
        val status = FreightEventStatus.COLLECTION_PACKAGE_PREPARING
        notify(trackId, freightId, status, source, currentPosition, description)
    }

    fun notifyCollectionPackageReadyToMove(trackId: UUID, freightId: UUID, source: String,
                                           currentPosition: String, description: String) {
        val status = FreightEventStatus.COLLECTION_PACKAGE_READY_TO_MOVE
        notify(trackId, freightId, status, source, currentPosition, description)
    }

    fun notifyPackageMoveStarted(trackId: UUID, freightId: UUID, source: String,
                                 currentPosition: String, description: String) {
        val status = FreightEventStatus.IN_TRANSIT_PACKAGE_STARTED
        notify(trackId, freightId, status, source, currentPosition, description)
    }

    fun notifyPackageMovingOnToNextStorage(trackId: UUID, freightId: UUID, source: String,
                                           currentPosition: String, description: String) {
        val status = FreightEventStatus.IN_TRANSIT_PACKAGE_MOVING_ON_TO_NEXT_STORAGE
        notify(trackId, freightId, status, source, currentPosition, description)
    }

    fun notifyPackageReceivedByStorage(trackId: UUID, freightId: UUID, source: String,
                                       currentPosition: String, description: String) {
        val status = FreightEventStatus.IN_TRANSIT_PACKAGE_RECEIVED_BY_STORAGE
        notify(trackId, freightId, status, source, currentPosition, description)
    }

    fun notifyPackageReachedFinalStorage(trackId: UUID, freightId: UUID, source: String,
                                         currentPosition: String, description: String) {
        val status = FreightEventStatus.IN_TRANSIT_PACKAGE_REACHED_FINAL_STORAGE
        notify(trackId, freightId, status, source, currentPosition, description)
    }

    fun notifyPackageOutForDelivery(trackId: UUID, freightId: UUID, source: String,
                                    currentPosition: String, description: String) {
        val status = FreightEventStatus.DELIVERY_OUT_FOR
        notify(trackId, freightId, status, source, currentPosition, description)
    }

    fun notifyPackageDeliveryStarted(trackId: UUID, freightId: UUID, source: String,
                               currentPosition: String, description: String) {
        val status = FreightEventStatus.DELIVERY_STARTED
        notify(trackId, freightId, status, source, currentPosition, description)
    }

    fun notifyPackageDelivered(trackId: UUID, freightId: UUID, source: String,
                               currentPosition: String, description: String) {
        val status = FreightEventStatus.DELIVERY_SUCCESS
        notify(trackId, freightId, status, source, currentPosition, description)
    }

    fun notifyPackageDeliveryFailed(trackId: UUID, freightId: UUID, source: String,
                                    currentPosition: String, description: String) {
        val status = FreightEventStatus.DELIVERY_FAILED
        notify(trackId, freightId, status, source, currentPosition, description)
    }

    fun notifyPackageDeliveryProcessReset(trackId: UUID, freightId: UUID, source: String,
                                          currentPosition: String, description: String) {
        val status = FreightEventStatus.DELIVERY_PROCESS_RESTART
        notify(trackId, freightId, status, source, currentPosition, description)
    }

    private fun notify(
            trackId: UUID, freightId: UUID, status: FreightEventStatus, source: String,
            currentPosition: String, description: String, lastUpdated: Instant = Instant.now()) {
        val payload = FreightEvent(trackId, freightId, status, source, currentPosition, description, lastUpdated)
        val message = EventMessage(trackId, payload)
        dedicatedProducerConnector.publish(message)
        logger.info("Event has been emitted to [${dedicatedProducerConnector.getId()}]: ${message.toJsonString()}")
    }
}