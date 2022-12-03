package boaentrega.gsl.order.domain.order.eventsourcing.command


import boaentrega.gsl.order.configuration.constants.EventSourcingBeanQualifiers
import boaentrega.gsl.support.eventsourcing.connectors.DedicatedProducerConnector
import boaentrega.gsl.support.eventsourcing.messages.CommandMessage
import boaentrega.gsl.support.extensions.ClassExtensions.logger
import boaentrega.gsl.support.extensions.ClassExtensions.toJsonString
import gsl.schemas.*
import org.springframework.beans.factory.annotation.Qualifier
import org.springframework.stereotype.Service
import java.time.Instant
import java.util.*

@Service
class FreightCommandService(
        @Qualifier(EventSourcingBeanQualifiers.FREIGHT_COMMAND_PRODUCER)
        private val dedicatedProducerConnector: DedicatedProducerConnector) {

    private val logger = logger()

    fun create(trackId: UUID, orderId: UUID, senderAddress: String, deliveryAddress: String) {
        val command = FreightCreateCommand(trackId, orderId, senderAddress, deliveryAddress, Instant.now())
        send(trackId, command)
    }

    fun pickupProduct(trackId: UUID, orderId: UUID, freightId: UUID,
                      pickupAddress: String, deliveryAddress: String) {
        val command = FreightPickupProductCommand(trackId, orderId, freightId,
                pickupAddress, deliveryAddress, Instant.now())
        send(trackId, command)
    }

    fun movePackage(trackId: UUID, orderId: UUID, freightId: UUID,
                    currentPosition: String, deliveryAddress: String) {
        val command = FreightMovePackageCommand(trackId, orderId, freightId,
                currentPosition, deliveryAddress, Instant.now())
        send(trackId, command)
    }

    fun deliverPackage(trackId: UUID, orderId: UUID, freightId: UUID,
                       currentPosition: String, deliveryAddress: String) {
        val command = FreightDeliverPackageCommand(trackId, orderId, freightId,
                currentPosition, deliveryAddress, Instant.now())
        send(trackId, command)
    }

    fun finish(trackId: UUID, orderId: UUID, freightId: UUID) {
        val command = FreightFinishCommand(trackId, orderId, freightId, Instant.now())
        send(trackId, command)
    }

    fun cancel(trackId: UUID, orderId: UUID, freightId: UUID, reason: String) {
        val command = FreightCancelCommand(trackId, orderId, freightId, reason, Instant.now())
        send(trackId, command)
    }

    private fun send(trackId: UUID, command: Any) {
        val message = CommandMessage(trackId, command)
        dedicatedProducerConnector.publish(message)
        logger.info("Command has been send to [${dedicatedProducerConnector.getId()}]: ${message.toJsonString()}")
    }

}