package boaentrega.gsl.order.domain.web

import boaentrega.gsl.order.AbstractWebTest
import boaentrega.gsl.order.configuration.constants.ResourcePaths
import boaentrega.gsl.order.domain.transportation.Transfer
import boaentrega.gsl.order.domain.transportation.TransferRepository
import boaentrega.gsl.order.domain.transportation.TransferService
import boaentrega.gsl.order.domain.transportation.TransferStatus
import boaentrega.gsl.order.domain.transportation.web.TransferController
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

internal class TransferApiTests : AbstractWebTest<Transfer>() {

    companion object {
        const val RESOURCE = ResourcePaths.TRANSPORT
    }

    @Autowired
    private lateinit var repository: TransferRepository

    @Autowired
    private lateinit var service: TransferService

    override fun createResource(): Any {
        return TransferController(service)
    }

    override fun getRepository() = repository
    override fun getEntityType() = Transfer::class.java
    override fun preProcessing(data: List<Transfer>) = data.forEach { it.status = TransferStatus.CREATED }
    override fun getResource() = RESOURCE

    @AfterEach
    fun reset() {
        repository.deleteAll()
    }

    @Test
    fun checkStatusById() {
        val id = entities.first().id
        restMockMvc.perform(get("$RESOURCE/{id}", id))
                .andExpect(jsonPath("\$.status").value(TransferStatus.CREATED.toString()))
    }

    @Test
    fun transfer() {
        val entity = entities.first() as Transfer
        val id = entity.id
        val contentMap = mapOf(
                "partnerId" to UUID.randomUUID().toString(),
                "storage" to entity.nextStorage)

        restMockMvc.perform(put("$RESOURCE/{id}/transfer", id)
                .contentType(MediaType.APPLICATION_JSON)
                .content(contentMap.toJsonString())
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk)
                .andExpect(
                        jsonPath("\$.status").value(TransferStatus.MOVING.toString()))
                .andExpect(
                        jsonPath("\$.partnerId").value(contentMap["partnerId"]))

        assertTotalMessagesAndReleaseThem()

        val eventContent = DummyProducerConnector.getMessageContent(FreightEvent::class)
        Assertions.assertEquals(FreightEventStatus.IN_TRANSIT_PACKAGE_MOVING_ON_TO_NEXT_STORAGE, eventContent?.status)
    }

    @Test
    fun receive() {
        val entity = entities.first() as Transfer
        val id = entity.id
        val contentMap = mapOf(
                "partnerId" to UUID.randomUUID().toString(),
                "storage" to entity.nextStorage)

        restMockMvc.perform(put("$RESOURCE/{id}/receive", id)
                .contentType(MediaType.APPLICATION_JSON)
                .content(contentMap.toJsonString())
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk)
                .andExpect(
                        jsonPath("\$.status").value(TransferStatus.IN_STORAGE.toString()))
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

        val contentMap = mapOf(
                "partnerId" to UUID.randomUUID().toString(),
                "storage" to entity.finalStorage)

        val result = restMockMvc.perform(put("$RESOURCE/{id}/receive", id)
                .contentType(MediaType.APPLICATION_JSON)
                .content(contentMap.toJsonString())
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk)
                .andExpect(
                        jsonPath("\$.status").value(TransferStatus.END_OF_ROUTE.toString()))
                .andExpect(
                        jsonPath("\$.partnerId").value(contentMap["partnerId"]))
                .andReturn()

        val transfer = result.response.contentAsString.toObject<Transfer>()

        assertTotalMessagesAndReleaseThem(2)

        val eventContent = DummyProducerConnector.getMessageContent(FreightEvent::class)
        Assertions.assertEquals(FreightEventStatus.IN_TRANSIT_PACKAGE_REACHED_FINAL_STORAGE, eventContent?.status)

        val commandContent = DummyProducerConnector.getMessageContent(FreightDeliverPackageCommand::class, 1)
        Assertions.assertEquals(transfer.trackId, commandContent?.trackId)
        Assertions.assertEquals(transfer.orderId, commandContent?.orderId)
        Assertions.assertEquals(transfer.freightId, commandContent?.freightId)
        Assertions.assertEquals(transfer.finalStorage, commandContent?.currentPosition)
        Assertions.assertEquals(transfer.deliveryAddress, commandContent?.deliveryAddress)
    }

}