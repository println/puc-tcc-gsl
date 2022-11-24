package boaentrega.gsl.delivery.domain.eventsourcing

import boaentrega.gsl.delivery.AbstractEventSourcingTest
import boaentrega.gsl.delivery.configuration.constants.EventSourcingBeanQualifiers
import boaentrega.gsl.delivery.configuration.constants.ResourcePaths
import boaentrega.gsl.delivery.domain.order.Order
import boaentrega.gsl.delivery.domain.order.OrderRepository
import boaentrega.gsl.delivery.domain.order.OrderStatus
import boaentrega.gsl.delivery.domain.order.web.OrderController
import boaentrega.gsl.delivery.domain.order.web.OrderWebService
import boaentrega.gsl.delivery.support.eventsourcing.connectors.dummy.DummyConsumerConnector
import boaentrega.gsl.delivery.support.eventsourcing.connectors.dummy.DummyProducerConnector
import boaentrega.gsl.delivery.support.eventsourcing.messages.CommandMessage
import gsl.schemas.*
import org.jeasy.random.EasyRandom
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.factory.annotation.Qualifier
import java.math.BigDecimal
import java.time.Instant
import java.util.*

class OrderCommandTest : AbstractEventSourcingTest() {

    companion object {
        const val RESOURCE = ResourcePaths.ORDER
    }

    @Autowired
    private lateinit var repository: OrderRepository

    @Autowired
    private lateinit var service: OrderWebService

    @Autowired
    @Qualifier(EventSourcingBeanQualifiers.ORDER_COMMAND_CONSUMER)
    private lateinit var consumerConnector: DummyConsumerConnector


    override fun createResource(): Any {
        return OrderController(service)
    }

    val easyRandom = EasyRandom()
    var entity: Order = easyRandom.nextObject(Order::class.java)

    @AfterEach
    fun reset() {
        repository.deleteAll()
    }

    @BeforeEach
    fun setupEntity() {
        val customerId = UUID.randomUUID()
        val pickupAddress = "pickupAddress"
        val destination = "destination"
        val data = Order(customerId, pickupAddress, destination)
        entity = repository.saveAndFlush(data)
    }

    @Test
    fun approved() {
        val command = Factory.createApproveCommand(entity)
        val message = CommandMessage(command.trackId, command)

        consumerConnector.consume(message)

        val order = checkAllFromApiAndGetFirst<Order>(RESOURCE)

        Assertions.assertEquals(command.trackId, order.id)
        Assertions.assertEquals(command.orderId, order.id)
        Assertions.assertTrue(command.value.compareTo(order.value) == 0)
        Assertions.assertEquals(OrderStatus.ACCEPTED, order.status)

        assertTotalMessagesAndReleaseThem(3)

        val eventMessage = DummyProducerConnector.getMessageContent(OrderEvent::class)
        Assertions.assertEquals(OrderEventStatus.ACCEPTED, eventMessage?.status)

        val commandMessage = DummyProducerConnector.getMessageContent(FreightCreateCommand::class)
        Assertions.assertEquals(order.id, commandMessage?.trackId)
        Assertions.assertEquals(order.id, commandMessage?.orderId)
        Assertions.assertEquals(order.pickupAddress, commandMessage?.senderAddress)
        Assertions.assertEquals(order.deliveryAddress, commandMessage?.deliveryAddress)

        assertDocumentReleased(order)
    }

    @Test
    fun refused() {
        val command = Factory.createRefuseCommand(entity)
        val message = CommandMessage(command.trackId, command)

        consumerConnector.consume(message)

        val order = checkAllFromApiAndGetFirst<Order>(RESOURCE)

        Assertions.assertEquals(command.trackId, order.id)
        Assertions.assertEquals(command.orderId, order.id)
        Assertions.assertEquals(command.reason, order.comment)
        Assertions.assertEquals(OrderStatus.REFUSED, order.status)

        assertTotalMessagesAndReleaseThem(2)

        val eventMessage = DummyProducerConnector.getMessageContent(OrderEvent::class)
        Assertions.assertEquals(OrderEventStatus.REFUSED, eventMessage?.status)

        assertDocumentReleased(order)
    }

    @Test
    fun checkApprovedDuplication() {
        val command = Factory.createApproveCommand(entity)
        val totalMessages = 3L
        val message = CommandMessage(command.trackId, command)
        repeat(3) {
            consumerConnector.consume(message)
        }
        checkAllFromApiAndGetFirst<Order>(RESOURCE)
        assertTotalMessagesAndReleaseThem(totalMessages)
    }

    @Test
    fun checkRefusedDuplication() {
        val command = Factory.createRefuseCommand(entity)
        val totalMessages = 2L
        val message = CommandMessage(command.trackId, command)
        repeat(3) {
            consumerConnector.consume(message)
        }
        checkAllFromApiAndGetFirst<Order>(RESOURCE)
        assertTotalMessagesAndReleaseThem(totalMessages)
    }

    private object Factory {
        fun createApproveCommand(entity: Order): OrderApproveCommand {
            val trackId = entity.id
            val orderId = entity.id
            val value = BigDecimal("10.20")
            val date = Instant.now()
            return OrderApproveCommand(trackId, orderId, value, date)
        }

        fun createRefuseCommand(entity: Order): OrderRefuseCommand {
            val trackId = entity.id
            val orderId = entity.id
            val reason = "Card invalid"
            val date = Instant.now()
            return OrderRefuseCommand(trackId, orderId, reason, date)
        }
    }

}