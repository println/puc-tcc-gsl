package boaentrega.gsl.freight

import boaentrega.gsl.freight.support.eventsourcing.connectors.dummy.DummyProducerConnector
import boaentrega.gsl.freight.support.extensions.ClassExtensions.toObject
import boaentrega.gsl.freight.support.jpa.AuditableModel
import boaentrega.gsl.freight.support.outbox.OutboxConnectorService
import boaentrega.gsl.freight.support.outbox.OutboxRepository
import boaentrega.gsl.freight.support.web.ResponsePage
import gsl.schemas.DocumentReleased
import org.hamcrest.Matchers
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.Assertions
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.MediaType
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders
import org.springframework.test.web.servlet.result.MockMvcResultMatchers
import java.lang.reflect.Modifier
import java.math.BigDecimal

abstract class AbstractEventSourcingTest : AbstractIntegrationTest() {
    @Autowired
    protected lateinit var outboxConnectorService: OutboxConnectorService

    @Autowired
    private lateinit var outboxRepository: OutboxRepository

    @AfterEach
    fun resetEventSourcing() {
        outboxRepository.deleteAll()
        DummyProducerConnector.resetDefaults()
    }

    protected fun assertTotalMessagesAndReleaseThem(total: Long = 1) {
        Assertions.assertEquals(total, outboxRepository.countByIsPublishedFalse())
        outboxConnectorService.releaseMessages()
        Assertions.assertEquals(total, outboxRepository.countByIsPublishedTrue())
    }

    protected final inline fun <reified T : AuditableModel<T>> assertDocumentReleased(entity: T) {
        val documentContent = DummyProducerConnector.getMessageContent(DocumentReleased::class)
        val document: T? = documentContent?.document?.toObject()
        val fields = entity.javaClass.declaredFields.filter { Modifier.isPublic(it.modifiers) }

        Assertions.assertEquals(entity.id, documentContent?.documentId)
        fields.forEach {
            when (it.type) {
                BigDecimal::class -> Assertions
                        .assertTrue((it.get(entity) as BigDecimal).compareTo(it.get(document) as BigDecimal) == 0)
                else -> Assertions.assertEquals(it.get(entity), it.get(document))
            }
        }
    }

    protected final inline fun <reified T : Any> checkAllFromApiAndGetFirst(resource: String, size: Int = 1): T {
        val result = restMockMvc.perform(MockMvcRequestBuilders.get(resource)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk)
                .andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(MockMvcResultMatchers.jsonPath("\$.content", Matchers.hasSize<String>(size)))
                .andReturn()

        val response = result.response
        val page = response.contentAsString.toObject<ResponsePage>()
        return page.getObject<T>()[0]
    }
}