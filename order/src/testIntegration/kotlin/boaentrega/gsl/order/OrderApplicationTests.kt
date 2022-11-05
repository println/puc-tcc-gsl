package boaentrega.gsl.order

import boaentrega.gsl.order.domain.order.OrderRepository
import boaentrega.gsl.order.domain.order.OrderService
import boaentrega.gsl.order.domain.order.web.OrderController
import boaentrega.gsl.order.support.outbox.OutboxConnectorService
import boaentrega.gsl.order.support.outbox.OutboxRepository
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.MediaType
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.header
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.status

class OrderApplicationTests : AbstractIntegrationTest() {

    @Autowired
    private lateinit var repository: OrderRepository

    @Autowired
    private lateinit var service: OrderService

    @Autowired
    private lateinit var outboxConnectorService: OutboxConnectorService

    @Autowired
    private lateinit var outboxRepository: OutboxRepository


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
        restMockMvc
                .perform(post("/")
                        .accept(MediaType.APPLICATION_JSON)
                        .content(""))
                .andExpect(status().isCreated)
                .andExpect(header().exists("Location"))

        assertTrue(repository.findAll().isNotEmpty())
        assertTrue(outboxRepository.getTop10ByIsPublishedFalse().isNotEmpty())

        outboxConnectorService.releaseMessages()

        assertTrue(outboxRepository.getTop10ByIsPublishedFalse().isEmpty())
    }

}
