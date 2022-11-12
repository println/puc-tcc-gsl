package boaentrega.gsl.order.domain.web

import boaentrega.gsl.order.AbstractWebTest
import boaentrega.gsl.order.configuration.constants.ResourcePaths
import boaentrega.gsl.order.domain.freight.Freight
import boaentrega.gsl.order.domain.order.Order
import boaentrega.gsl.order.domain.order.OrderRepository
import boaentrega.gsl.order.domain.order.OrderService
import boaentrega.gsl.order.domain.order.OrderStatus
import boaentrega.gsl.order.domain.order.web.OrderController
import boaentrega.gsl.order.domain.order.web.OrderDto
import boaentrega.gsl.order.support.eventsourcing.connectors.dummy.DummyProducerConnector
import boaentrega.gsl.order.support.extensions.ClassExtensions.toJsonString
import boaentrega.gsl.order.support.extensions.ClassExtensions.toObject
import gsl.schemas.DocumentReleased
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

    companion object {
        const val RESOURCE = ResourcePaths.ORDER
    }

    @Autowired
    private lateinit var repository: OrderRepository

    @Autowired
    private lateinit var service: OrderService


    override fun getRepository() = repository
    override fun getEntityType() = Order::class.java
    override fun preProcessing(data: List<Order>) = data.forEach { it.status = OrderStatus.WAITING_PAYMENT }
    override fun getResource() = RESOURCE

    override fun createResource(): Any {
        return OrderController(service)
    }

    @AfterEach
    fun reset() {
        repository.deleteAll()
    }

    @Test
    fun createOrder() {
        val customerId = UUID.randomUUID()
        val pickupAddress = "pickupAddress"
        val destination = "destination"

        val data = OrderDto(customerId, pickupAddress, destination)

        val result = restMockMvc
                .perform(post(RESOURCE)
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(data.toJsonString()))
                .andExpect(status().isCreated)
                .andExpect(header().exists("Location"))
                .andReturn()

        val order: Order = result.response.contentAsString.toObject()

        assertTotalMessagesAndReleaseThem(2)

        val eventContent = DummyProducerConnector.getMessageContent(OrderEvent::class)
        Assertions.assertEquals(OrderEventStatus.WAITING_PAYMENT, eventContent?.status)
        Assertions.assertEquals(order.id, eventContent?.trackId)

        assertDocumentReleased(order)
    }


}
