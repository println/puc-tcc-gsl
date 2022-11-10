package boaentrega.gsl.order.eventsourcing

import boaentrega.gsl.order.AbstractIntegrationTest
import boaentrega.gsl.order.configuration.constants.EventSourcingBeansConstants
import boaentrega.gsl.order.domain.order.Order
import boaentrega.gsl.order.domain.order.OrderRepository
import boaentrega.gsl.order.domain.order.OrderService
import boaentrega.gsl.order.domain.order.OrderStatus
import boaentrega.gsl.order.domain.order.web.OrderController
import boaentrega.gsl.order.support.eventsourcing.connectors.dummy.DummyConsumerConnector
import boaentrega.gsl.order.support.eventsourcing.connectors.dummy.DummyProducerConnector
import boaentrega.gsl.order.support.eventsourcing.messages.CommandMessage
import boaentrega.gsl.order.support.extensions.ClassExtensions.toObject
import boaentrega.gsl.order.support.outbox.OutboxConnectorService
import boaentrega.gsl.order.support.outbox.OutboxRepository
import boaentrega.gsl.order.support.web.ResponsePage
import gsl.schemas.*
import org.hamcrest.Matchers
import org.jeasy.random.EasyRandom
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.factory.annotation.Qualifier
import org.springframework.http.MediaType
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders
import org.springframework.test.web.servlet.result.MockMvcResultMatchers
import java.math.BigDecimal
import java.time.LocalDate
import java.util.*

class OrderCommandTest : AbstractIntegrationTest() {

    companion object {
        const val RESOURCE = "/"
    }

    @Autowired
    private lateinit var repository: OrderRepository

    @Autowired
    private lateinit var service: OrderService

    @Autowired
    private lateinit var outboxRepository: OutboxRepository

    @Autowired
    private lateinit var outboxConnectorService: OutboxConnectorService

    @Autowired
    @Qualifier(EventSourcingBeansConstants.ORDER_COMMAND_CONSUMER)
    private lateinit var consumerConnector: DummyConsumerConnector


    override fun createResource(): Any {
        return OrderController(service)
    }

    val easyRandom = EasyRandom()
    var entity: Order = easyRandom.nextObject(Order::class.java)

    @AfterEach
    fun reset() {
        repository.deleteAll()
        outboxRepository.deleteAll()
        DummyProducerConnector.clearMessages()
    }

    @BeforeEach
    fun setupCommand() {
        val customerId = UUID.randomUUID()
        val pickupAddress = "pickupAddress"
        val destination = "destination"
        val data = Order(customerId, pickupAddress, destination)
        entity = repository.saveAndFlush(data)
    }

    @Test
    fun approved() {
        val command = createApproveCommand()
        val message = CommandMessage(command.trackId, command)

        consumerConnector.consume(message)

        val order = findAll()

        Assertions.assertEquals(command.trackId, order.id)
        Assertions.assertEquals(command.orderId, order.id)
        Assertions.assertTrue(command.value.compareTo(order.value) == 0)
        Assertions.assertEquals(OrderStatus.ACCEPTED, order.status)

        releaseMessages()
        checkEventSourcingEvents(OrderEventStatus.ACCEPTED)
        checkEventSourcingCommands(order)
    }

    @Test
    fun refused() {
        val command = createRefuseCommand()
        val message = CommandMessage(command.trackId, command)

        consumerConnector.consume(message)

        val order = findAll()

        Assertions.assertEquals(command.trackId, order.id)
        Assertions.assertEquals(command.orderId, order.id)
        Assertions.assertEquals(command.reason, order.comment)
        Assertions.assertEquals(OrderStatus.REFUSED, order.status)

        Assertions.assertEquals(1, outboxRepository.getTop10ByIsPublishedFalse().size)
        releaseMessages()
        checkEventSourcingEvents(OrderEventStatus.REFUSED)
    }

    @Test
    fun checkApprovedDuplication() {
        val command = createApproveCommand()
        val totalMessages = 2
        val message = CommandMessage(command.trackId, command)

        consumerConnector.consume(message)
        consumerConnector.consume(message)

        val pickupRequest = findAll()

        Assertions.assertEquals(totalMessages, outboxRepository.getTop10ByIsPublishedFalse().size)
        releaseMessages()
    }

    @Test
    fun checkRefusedDuplication() {
        val command = createRefuseCommand()
        val totalMessages = 1
        val message = CommandMessage(command.trackId, command)

        consumerConnector.consume(message)
        consumerConnector.consume(message)

        val pickupRequest = findAll()

        Assertions.assertEquals(totalMessages, outboxRepository.getTop10ByIsPublishedFalse().size)
        releaseMessages()
    }

    private fun createApproveCommand(): OrderApproveCommand {
        val trackId = entity.id
        val orderId = entity.id
        val value = BigDecimal("10.20")
        val date = LocalDate.now()
        return OrderApproveCommand(trackId, orderId, value, date)
    }

    private fun createRefuseCommand(): OrderRefuseCommand {
        val trackId = entity.id
        val orderId = entity.id
        val reason = "Card invalid"
        val date = LocalDate.now()
        return OrderRefuseCommand(trackId, orderId, reason, date)
    }

    private fun findAll(): Order {
        val result = restMockMvc.perform(MockMvcRequestBuilders.get(RESOURCE)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk)
                .andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(MockMvcResultMatchers.jsonPath("\$.content", Matchers.hasSize<String>(1)))
                .andReturn()

        val response = result.response
        val page = response.contentAsString.toObject<ResponsePage>()
        return page.getObject<Order>()[0]
    }

    private fun checkEventSourcingCommands(order: Order) {
        val message = DummyProducerConnector.findAll(FreightCreateCommand::class.java).first()
        val command = message.content.toObject<FreightCreateCommand>()

        Assertions.assertEquals(order.id, command.trackId)
        Assertions.assertEquals(order.id, command.orderId)
        Assertions.assertEquals(order.pickupAddress, command.pickupAddress)
        Assertions.assertEquals(order.deliveryAddress, command.deliveryAddress)
    }

    private fun checkEventSourcingEvents(status: OrderEventStatus) {
        val message = DummyProducerConnector.findAll(OrderEvent::class.java).first()
        val event = message.content.toObject<OrderEvent>()
        Assertions.assertEquals(status, event.status)
    }

    private fun releaseMessages() {
        Assertions.assertTrue(outboxRepository.getTop10ByIsPublishedFalse().isNotEmpty())
        outboxConnectorService.releaseMessages()
        Assertions.assertTrue(outboxRepository.getTop10ByIsPublishedFalse().isEmpty())
    }

}