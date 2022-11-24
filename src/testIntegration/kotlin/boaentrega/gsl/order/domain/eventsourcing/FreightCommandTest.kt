package boaentrega.gsl.order.domain.eventsourcing

import boaentrega.gsl.order.AbstractEventSourcingTest
import boaentrega.gsl.order.configuration.constants.EventSourcingBeanQualifiers
import boaentrega.gsl.order.configuration.constants.ResourcePaths
import boaentrega.gsl.order.domain.freight.Freight
import boaentrega.gsl.order.domain.freight.FreightRepository
import boaentrega.gsl.order.domain.freight.FreightService
import boaentrega.gsl.order.domain.freight.FreightStatus
import boaentrega.gsl.order.domain.freight.web.FreightController
import boaentrega.gsl.order.domain.order.Order
import boaentrega.gsl.order.support.eventsourcing.connectors.dummy.DummyConsumerConnector
import boaentrega.gsl.order.support.eventsourcing.connectors.dummy.DummyProducerConnector
import boaentrega.gsl.order.support.eventsourcing.messages.CommandMessage
import gsl.schemas.*
import org.jeasy.random.EasyRandom
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.factory.annotation.Qualifier
import java.time.Instant
import java.util.*

class FreightCommandTest : AbstractEventSourcingTest() {

    companion object {
        const val RESOURCE = ResourcePaths.FREIGHT
    }

    @Autowired
    private lateinit var repository: FreightRepository

    @Autowired
    private lateinit var service: FreightService

    @Autowired
    @Qualifier(EventSourcingBeanQualifiers.FREIGHT_COMMAND_CONSUMER)
    private lateinit var consumerConnector: DummyConsumerConnector


    override fun createResource(): Any {
        return FreightController(service)
    }

    val easyRandom = EasyRandom()
    var orderEntity: Order = easyRandom.nextObject(Order::class.java)
    var entity: Freight = easyRandom.nextObject(Freight::class.java)

    @AfterEach
    fun reset() {
        repository.deleteAll()
    }

    fun setupEntity(status: FreightStatus) {
        val orderId = UUID.randomUUID()
        val senderAddress = "senderAddress"
        val deliveryAddress = "deliveryAddress"
        val data = Freight(orderId, orderId, senderAddress, deliveryAddress, senderAddress, status)
        entity = repository.saveAndFlush(data)
    }

    @Test
    fun create() {
        val command = Factory.createCreateCommand(orderEntity)
        val message = CommandMessage(command.trackId, command)

        consumerConnector.consume(message)

        val freight = checkAllFromApiAndGetFirst<Freight>(RESOURCE)

        Assertions.assertEquals(command.trackId, freight.trackId)
        Assertions.assertEquals(command.orderId, freight.orderId)
        Assertions.assertEquals(command.senderAddress, freight.senderAddress)
        Assertions.assertEquals(command.deliveryAddress, freight.deliveryAddress)
        Assertions.assertEquals(FreightStatus.CREATED, freight.status)

        assertTotalMessagesAndReleaseThem(3)

        var eventContent = DummyProducerConnector.getMessageContent(FreightEvent::class)
        Assertions.assertEquals(FreightEventStatus.CREATED, eventContent?.status)

        val commandContent = DummyProducerConnector.getMessageContent(FreightPickupProductCommand::class)
        Assertions.assertEquals(command.trackId, commandContent?.trackId)
        Assertions.assertEquals(command.orderId, commandContent?.orderId)
        Assertions.assertEquals(command.senderAddress, commandContent?.pickupAddress)
        Assertions.assertEquals(command.deliveryAddress, commandContent?.deliveryAddress)

        assertDocumentReleased(freight)
    }

    @Test
    fun checkCreateDuplication() {
        val command = Factory.createCreateCommand(orderEntity)
        val message = CommandMessage(command.trackId, command)
        repeat(3) {
            consumerConnector.consume(message)
        }
        checkAllFromApiAndGetFirst<Freight>(RESOURCE)
        assertTotalMessagesAndReleaseThem(3)
    }

    @Test
    fun finish() {
        setupEntity(FreightStatus.DELIVERY_SUCCESS)

        val command = Factory.createFinishCommand(entity)
        val message = CommandMessage(command.trackId, command)

        consumerConnector.consume(message)

        val freight = checkAllFromApiAndGetFirst<Freight>(RESOURCE)

        Assertions.assertEquals(entity.trackId, freight.trackId)
        Assertions.assertEquals(entity.orderId, freight.orderId)
        Assertions.assertEquals(entity.senderAddress, freight.senderAddress)
        Assertions.assertEquals(entity.deliveryAddress, freight.deliveryAddress)
        Assertions.assertEquals(entity.deliveryAddress, freight.currentPosition)
        Assertions.assertEquals(FreightStatus.FINISHED, freight.status)

        assertTotalMessagesAndReleaseThem(2)

        var eventContent = DummyProducerConnector.getMessageContent(FreightEvent::class)
        Assertions.assertEquals(FreightEventStatus.FINISHED, eventContent?.status)

        assertDocumentReleased(freight)
    }

    @Test
    fun checkFinishDuplication() {
        setupEntity(FreightStatus.DELIVERY_SUCCESS)
        val command = Factory.createFinishCommand(entity)
        val message = CommandMessage(command.trackId, command)
        repeat(3) {
            consumerConnector.consume(message)
        }
        checkAllFromApiAndGetFirst<Freight>(RESOURCE)
        assertTotalMessagesAndReleaseThem(2)
    }

    private object Factory {
        fun createCreateCommand(entity: Order): FreightCreateCommand {
            val trackId = entity.id
            val orderId = entity.id
            val senderAddress = "address from"
            val deliveryAddress = "address to"
            val date = Instant.now()
            return FreightCreateCommand(trackId, orderId, senderAddress, deliveryAddress, date)
        }

        fun createFinishCommand(entity: Freight): FreightFinishCommand {
            val date = Instant.now()
            return FreightFinishCommand(entity.trackId, entity.orderId, entity.id, date)
        }
    }


}