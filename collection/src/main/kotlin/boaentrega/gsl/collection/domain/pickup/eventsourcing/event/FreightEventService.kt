package boaentrega.gsl.collection.domain.pickup.eventsourcing.event

import boaentrega.gsl.collection.configuration.constants.EventSourcingBeanQualifiers
import boaentrega.gsl.collection.support.eventsourcing.connectors.DedicatedProducerConnector
import boaentrega.gsl.collection.support.eventsourcing.messages.EventMessage
import boaentrega.gsl.collection.support.extensions.ClassExtensions.logger
import boaentrega.gsl.collection.support.extensions.ClassExtensions.toJsonString
import gsl.schemas.FreightEvent
import gsl.schemas.FreightEventStatus
import org.springframework.beans.factory.annotation.Qualifier
import org.springframework.stereotype.Service
import java.time.Instant
import java.util.*

@Service
class FreightEventService(
        @Qualifier(EventSourcingBeanQualifiers.FREIGHT_EVENT_PRODUCER)
        private val dedicatedProducerConnector: DedicatedProducerConnector) {

    private val logger = logger()

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

    private fun notify(
            trackId: UUID, freightId: UUID, status: FreightEventStatus, source: String,
            currentPosition: String, description: String, lastUpdated: Instant = Instant.now()) {
        val payload = FreightEvent(trackId, freightId, status, source, currentPosition, description, lastUpdated)
        val message = EventMessage(trackId, payload)
        dedicatedProducerConnector.publish(message)
        logger.info("Event has been emitted to [${dedicatedProducerConnector.getId()}]: ${message.toJsonString()}")
    }
}