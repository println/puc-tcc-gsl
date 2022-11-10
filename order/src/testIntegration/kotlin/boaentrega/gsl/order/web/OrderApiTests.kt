package boaentrega.gsl.order.web

import boaentrega.gsl.order.AbstractWebTest
import boaentrega.gsl.order.domain.order.Order
import boaentrega.gsl.order.domain.order.OrderRepository
import boaentrega.gsl.order.domain.order.OrderService
import boaentrega.gsl.order.domain.order.OrderStatus
import boaentrega.gsl.order.domain.order.web.OrderController
import boaentrega.gsl.order.domain.order.web.OrderDto
import boaentrega.gsl.order.support.eventsourcing.connectors.dummy.DummyProducerConnector
import boaentrega.gsl.order.support.extensions.ClassExtensions.toJsonString
import boaentrega.gsl.order.support.extensions.ClassExtensions.toObject
import boaentrega.gsl.order.support.outbox.OutboxConnectorService
import boaentrega.gsl.order.support.outbox.OutboxRepository
import gsl.schemas.OrderEvent
import gsl.schemas.OrderEventStatus
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.MediaType
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.header
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.status
import java.util.*

class OrderApiTests : AbstractWebTest<Order>() {

    @Autowired
    private lateinit var repository: OrderRepository

    @Autowired
    private lateinit var service: OrderService

    @Autowired
    private lateinit var outboxConnectorService: OutboxConnectorService

    @Autowired
    private lateinit var outboxRepository: OutboxRepository

    override fun getRepository() = repository
    override fun getEntityType() = Order::class.java
    override fun preProcessing(data: List<Order>) = data.forEach { it.status = OrderStatus.WAITING_PAYMENT }
    override fun getResource() = "/"

    override fun createResource(): Any {
        return OrderController(service)
    }

    @AfterEach
    fun reset() {
        repository.deleteAll()
        outboxRepository.deleteAll()
    }

    @Test
    fun createOrder() {
        val customerId = UUID.randomUUID()
        val pickupAddress = "pickupAddress"
        val destination = "destination"

        val data = OrderDto(customerId, pickupAddress, destination)

        val result = restMockMvc
                .perform(post(getResource())
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(data.toJsonString()))
                .andExpect(status().isCreated)
                .andExpect(header().exists("Location"))
                .andReturn()

        val order: Order = result.response.contentAsString.toObject()

        releaseMessages()
        checkEventSourcingEvents(order.id!!, OrderEventStatus.WAITING_PAYMENT)
    }

    private fun checkEventSourcingEvents(orderId: UUID, status: OrderEventStatus) {
        val message = DummyProducerConnector.findAll(OrderEvent::class.java).first()
        val event = message.content.toObject<OrderEvent>()
        Assertions.assertEquals(status, event.status)
        Assertions.assertEquals(orderId, event.trackId)
    }

    private fun releaseMessages() {
        Assertions.assertTrue(outboxRepository.getTop10ByIsPublishedFalse().isNotEmpty())
        outboxConnectorService.releaseMessages()
        Assertions.assertTrue(outboxRepository.getTop10ByIsPublishedFalse().isEmpty())
    }

}
