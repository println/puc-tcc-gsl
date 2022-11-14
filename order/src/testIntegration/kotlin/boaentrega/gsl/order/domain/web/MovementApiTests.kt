package boaentrega.gsl.order.domain.web

import boaentrega.gsl.order.AbstractWebTest
import boaentrega.gsl.order.configuration.constants.ResourcePaths
import boaentrega.gsl.order.domain.transportation.Movement
import boaentrega.gsl.order.domain.transportation.MovementRepository
import boaentrega.gsl.order.domain.transportation.MovementService
import boaentrega.gsl.order.domain.transportation.MovementStatus
import boaentrega.gsl.order.domain.transportation.web.MovementController
import boaentrega.gsl.order.support.eventsourcing.connectors.dummy.DummyProducerConnector
import boaentrega.gsl.order.support.extensions.ClassExtensions.toJsonString
import boaentrega.gsl.order.support.extensions.ClassExtensions.toObject
import gsl.schemas.FreightDeliverPackageCommand
import gsl.schemas.FreightEvent
import gsl.schemas.FreightEventStatus
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.MediaType
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.status
import java.util.*

internal class MovementApiTests : AbstractWebTest<Movement>() {

    companion object {
        const val RESOURCE = ResourcePaths.TRANSPORT
    }

    @Autowired
    private lateinit var repository: MovementRepository

    @Autowired
    private lateinit var service: MovementService

    override fun createResource(): Any {
        return MovementController(service)
    }

    override fun getRepository() = repository
    override fun getEntityType() = Movement::class.java
    override fun preProcessing(data: List<Movement>) = data.forEach { it.status = MovementStatus.CREATED }
    override fun getResource() = RESOURCE

    @AfterEach
    fun reset() {
        repository.deleteAll()
    }

    @Test
    fun checkStatusById() {
        val id = entities.first().id
        restMockMvc.perform(get("$RESOURCE/{id}", id))
                .andExpect(jsonPath("\$.status").value(MovementStatus.CREATED.toString()))
    }

    @Test
    fun transfer() {
        val id = entities.first().id
        val contentMap = mapOf("partnerId" to UUID.randomUUID().toString())
        restMockMvc.perform(put("$RESOURCE/{id}/transfer", id)
                .contentType(MediaType.APPLICATION_JSON)
                .content(contentMap.toJsonString())
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk)
                .andExpect(
                        jsonPath("\$.status").value(MovementStatus.MOVING.toString()))
                .andExpect(
                        jsonPath("\$.partnerId").value(contentMap["partnerId"]))

        assertTotalMessagesAndReleaseThem()

        val eventContent = DummyProducerConnector.getMessageContent(FreightEvent::class)
        Assertions.assertEquals(FreightEventStatus.IN_TRANSIT_PACKAGE_MOVING_ON_TO_NEXT_STORAGE, eventContent?.status)
    }

    @Test
    fun receive() {
        val id = entities.first().id
        val contentMap = mapOf("partnerId" to UUID.randomUUID().toString())
        restMockMvc.perform(put("$RESOURCE/{id}/receive", id)
                .contentType(MediaType.APPLICATION_JSON)
                .content(contentMap.toJsonString())
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk)
                .andExpect(
                        jsonPath("\$.status").value(MovementStatus.IN_STORAGE.toString()))
                .andExpect(
                        jsonPath("\$.partnerId").value(contentMap["partnerId"]))

        assertTotalMessagesAndReleaseThem()

        val eventContent = DummyProducerConnector.getMessageContent(FreightEvent::class)
        Assertions.assertEquals(FreightEventStatus.IN_TRANSIT_PACKAGE_RECEIVED_BY_STORAGE, eventContent?.status)
    }

    @Test
    fun receiveInFinalStorage() {
        val id = entities.first().id

        val entity = repository.findById(id!!).get()
        entity.nextStorage = entity.finalStorage
        entity.currentPosition = entity.finalStorage
        repository.saveAndFlush(entity)

        val contentMap = mapOf("partnerId" to UUID.randomUUID().toString())
        val result = restMockMvc.perform(put("$RESOURCE/{id}/receive", id)
                .contentType(MediaType.APPLICATION_JSON)
                .content(contentMap.toJsonString())
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk)
                .andExpect(
                        jsonPath("\$.status").value(MovementStatus.END_OF_ROUTE.toString()))
                .andExpect(
                        jsonPath("\$.partnerId").value(contentMap["partnerId"]))
                .andReturn()

        val movement = result.response.contentAsString.toObject<Movement>()

        assertTotalMessagesAndReleaseThem(2)

        val eventContent = DummyProducerConnector.getMessageContent(FreightEvent::class)
        Assertions.assertEquals(FreightEventStatus.IN_TRANSIT_PACKAGE_REACHED_FINAL_STORAGE, eventContent?.status)

        val commandContent = DummyProducerConnector.getMessageContent(FreightDeliverPackageCommand::class, 1)
        Assertions.assertEquals(movement.trackId, commandContent?.trackId)
        Assertions.assertEquals(movement.orderId, commandContent?.orderId)
        Assertions.assertEquals(movement.freightId, commandContent?.freightId)
        Assertions.assertEquals(movement.finalStorage, commandContent?.currentPosition)
        Assertions.assertEquals(movement.deliveryAddress, commandContent?.deliveryAddress)
    }

}