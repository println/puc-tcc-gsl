package boaentrega.gsl.order.domain.order.eventsourcing.command


import boaentrega.gsl.order.configuration.constants.EventSourcingBeansConstants
import boaentrega.gsl.order.support.eventsourcing.connectors.DedicatedProducerConnector
import boaentrega.gsl.order.support.eventsourcing.messages.CommandMessage
import boaentrega.gsl.order.support.extensions.ClassExtensions.logger
import boaentrega.gsl.order.support.extensions.ClassExtensions.toJsonString
import gsl.schemas.*
import org.apache.avro.specific.SpecificRecord
import org.springframework.beans.factory.annotation.Qualifier
import org.springframework.stereotype.Service
import java.time.LocalDate
import java.util.*

@Service
class FreightCommandService(
        @Qualifier(EventSourcingBeansConstants.FREIGHT_COMMAND_PRODUCER)
        private val dedicatedProducerConnector: DedicatedProducerConnector) {

    private val logger = logger()

    fun create(trackId: UUID, orderId: UUID, pickupAddress: String, deliveryAddress: String) {
        val command = FreightCreateCommand(trackId, orderId, pickupAddress, deliveryAddress, LocalDate.now())
        send(trackId, command)
    }

    fun pickupProduct(trackId: UUID, orderId: UUID, freightId: UUID, pickupAddress: String, destination: String) {
        val command = FreightPickupProductCommand(trackId, orderId, freightId, pickupAddress, destination, LocalDate.now())
        send(trackId, command)
    }

    fun movePackage(trackId: UUID, orderId: UUID, freightId: UUID, from: String, to: String) {
        val command = FreightMovePackageCommand(trackId, orderId, freightId, from, to, LocalDate.now())
        send(trackId, command)
    }

    fun deliverPackage(trackId: UUID, orderId: UUID, freightId: UUID, deliveryAddress: String) {
        val command = FreightDeliverPackageCommand(trackId, orderId, freightId, deliveryAddress, LocalDate.now())
        send(trackId, command)
    }

    fun finish(trackId: UUID, orderId: UUID, freightId: UUID) {
        val command = FreightFinishCommand(trackId, orderId, freightId, LocalDate.now())
        send(trackId, command)
    }

    fun cancel(trackId: UUID, orderId: UUID, freightId: UUID, reason: String) {
        val command = FreightCancelCommand(trackId, orderId, freightId, reason, LocalDate.now())
        send(trackId, command)
    }

    private fun send(trackId: UUID, command: Any) {
        val message = CommandMessage(trackId, command)
        dedicatedProducerConnector.publish(message)
        logger.info("Command has been send to [${dedicatedProducerConnector.getId()}]: ${message.toJsonString()}")
    }

}