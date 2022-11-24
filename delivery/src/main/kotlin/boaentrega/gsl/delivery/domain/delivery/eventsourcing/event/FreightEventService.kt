package boaentrega.gsl.delivery.domain.delivery.eventsourcing.event

import boaentrega.gsl.delivery.configuration.constants.EventSourcingBeanQualifiers
import boaentrega.gsl.delivery.support.eventsourcing.connectors.DedicatedProducerConnector
import boaentrega.gsl.delivery.support.eventsourcing.messages.EventMessage
import boaentrega.gsl.delivery.support.extensions.ClassExtensions.logger
import boaentrega.gsl.delivery.support.extensions.ClassExtensions.toJsonString
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