package boaentrega.gsl.order.domain.web

import boaentrega.gsl.order.AbstractWebTest
import boaentrega.gsl.order.configuration.constants.ResourcePaths
import boaentrega.gsl.order.domain.collection.PickupRequestStatus
import boaentrega.gsl.order.domain.freight.Freight
import boaentrega.gsl.order.domain.freight.FreightRepository
import boaentrega.gsl.order.domain.freight.FreightService
import boaentrega.gsl.order.domain.freight.FreightStatus
import boaentrega.gsl.order.domain.freight.web.FreightController
import boaentrega.gsl.order.domain.order.Order
import boaentrega.gsl.order.domain.order.web.OrderDto
import boaentrega.gsl.order.support.eventsourcing.connectors.dummy.DummyProducerConnector
import boaentrega.gsl.order.support.extensions.ClassExtensions.toJsonString
import boaentrega.gsl.order.support.extensions.ClassExtensions.toObject
import gsl.schemas.OrderEvent
import gsl.schemas.OrderEventStatus
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.MediaType
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post
import org.springframework.test.web.servlet.result.MockMvcResultMatchers
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.header
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.status
import java.util.*

class FreightApiTests : AbstractWebTest<Freight>() {

    companion object {
        const val RESOURCE = ResourcePaths.FREIGHT
    }

    @Autowired
    private lateinit var repository: FreightRepository

    @Autowired
    private lateinit var service: FreightService


    override fun getRepository() = repository
    override fun getEntityType() = Freight::class.java
    override fun preProcessing(data: List<Freight>) = data.forEach { it.status = FreightStatus.CREATED }
    override fun getResource() = RESOURCE

    override fun createResource(): Any {
        return FreightController(service)
    }

    @AfterEach
    fun reset() {
        repository.deleteAll()
    }

    @Test
    fun checkStatusById() {
        val id = entities.first().id
        restMockMvc.perform(MockMvcRequestBuilders.get("${RESOURCE}/{id}", id))
                .andExpect(MockMvcResultMatchers.jsonPath("\$.status").value(FreightStatus.CREATED.toString()))
    }

}
