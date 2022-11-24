package boaentrega.gsl.order.domain.web

import boaentrega.gsl.order.AbstractWebTest
import boaentrega.gsl.order.configuration.constants.ResourcePaths
import boaentrega.gsl.order.domain.delivery.Delivery
import boaentrega.gsl.order.domain.delivery.DeliveryRepository
import boaentrega.gsl.order.domain.delivery.DeliveryStatus
import boaentrega.gsl.order.domain.delivery.web.DeliveryController
import boaentrega.gsl.order.domain.delivery.web.DeliveryWebService
import boaentrega.gsl.order.domain.transportation.TransferStatus
import boaentrega.gsl.order.support.eventsourcing.connectors.dummy.DummyProducerConnector
import boaentrega.gsl.order.support.extensions.ClassExtensions.toJsonString
import boaentrega.gsl.order.support.extensions.ClassExtensions.toObject
import gsl.schemas.FreightEvent
import gsl.schemas.FreightEventStatus
import gsl.schemas.FreightFinishCommand
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.MediaType
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.status
import java.util.*

internal class DeliveryApiTests : AbstractWebTest<Delivery>() {

    companion object {
        const val RESOURCE = ResourcePaths.DELIVERY
    }

    @Autowired
    private lateinit var repository: DeliveryRepository

    @Autowired
    private lateinit var service: DeliveryWebService

    override fun createResource(): Any {
        return DeliveryController(service)
    }

    override fun getRepository() = repository
    override fun getEntityType() = Delivery::class.java
    override fun preProcessing(data: Delivery) {
        data.status = DeliveryStatus.CREATED
    }

    override fun getResource() = RESOURCE

    @AfterEach
    fun reset() {
        repository.deleteAll()
    }

    @Test
    fun checkStatusById() {
        val id = entities.first().id
        restMockMvc.perform(get("$RESOURCE/{id}", id))
                .andExpect(jsonPath("\$.status").value(DeliveryStatus.CREATED.toString()))
    }

    @Test
    fun addPreferredTimeForDelivery() {
        val id = entities.first().id
        val contentMap = mapOf("time" to "11:11")
        restMockMvc.perform(put("$RESOURCE/{id}/time", id)
                .contentType(MediaType.APPLICATION_JSON)
                .content(contentMap.toJsonString())
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk)
                .andExpect(jsonPath("\$.status").value(DeliveryStatus.CREATED.toString()))
                .andExpect(jsonPath("\$.preferredDeliveryTime").value(contentMap["time"]))

        assertTotalMessagesAndReleaseThem(0)
    }

    @Test
    fun takePackageToDelivery() {
        reloadData {
            it.status = DeliveryStatus.CREATED
            it.currentPosition = it.storageAddress
        }
        val id = entities.first().id
        val contentMap = mapOf("partnerId" to UUID.randomUUID().toString())
        restMockMvc.perform(put("$RESOURCE/{id}/package", id)
                .contentType(MediaType.APPLICATION_JSON)
                .content(contentMap.toJsonString())
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk)
                .andExpect(
                        jsonPath("\$.status").value(DeliveryStatus.OUT_FOR_DELIVERY.toString()))
                .andExpect(
                        jsonPath("\$.partnerId").value(contentMap["partnerId"]))

        assertTotalMessagesAndReleaseThem()

        val eventContent = DummyProducerConnector.getMessageContent(FreightEvent::class)
        Assertions.assertEquals(FreightEventStatus.DELIVERY_OUT_FOR, eventContent?.status)
    }

    @Test
    fun returnPackage() {
        reloadData {
            it.status = DeliveryStatus.FAILED_DELIVERY_ATTEMPT
            it.currentPosition = it.deliveryAddress+'-'+it.storageAddress
        }
        val id = entities.first().id
        restMockMvc.perform(delete("$RESOURCE/{id}/package", id)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk)
                .andExpect(jsonPath("\$.status").value(DeliveryStatus.RETRY_DELIVERY.toString()))

        assertTotalMessagesAndReleaseThem()

        val eventContent = DummyProducerConnector.getMessageContent(FreightEvent::class)
        Assertions.assertEquals(FreightEventStatus.DELIVERY_PROCESS_RESTART, eventContent?.status)
    }

    @Test
    fun markAsDeliverySuccessfully() {
        reloadData {
            it.status = DeliveryStatus.OUT_FOR_DELIVERY
            it.currentPosition = it.storageAddress+'-'+it.deliveryAddress
        }
        val id = entities.first().id
        val result = restMockMvc.perform(put("$RESOURCE/{id}/delivery", id)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk)
                .andExpect(
                        jsonPath("\$.status").value(DeliveryStatus.SUCCESSFULLY_DELIVERED.toString()))
                .andReturn()

        val delivery = result.response.contentAsString.toObject<Delivery>()

        assertTotalMessagesAndReleaseThem(2)

        val eventContent = DummyProducerConnector.getMessageContent(FreightEvent::class)
        Assertions.assertEquals(FreightEventStatus.DELIVERY_SUCCESS, eventContent?.status)

        val commandContent = DummyProducerConnector.getMessageContent(FreightFinishCommand::class, 1)
        Assertions.assertEquals(delivery.trackId, commandContent?.trackId)
        Assertions.assertEquals(delivery.orderId, commandContent?.orderId)
        Assertions.assertEquals(delivery.freightId, commandContent?.freightId)
    }

    @Test
    fun markAsDeliveryFailed() {
        reloadData {
            it.status = DeliveryStatus.OUT_FOR_DELIVERY
            it.currentPosition = it.storageAddress+'-'+it.deliveryAddress
        }
        val id = entities.first().id
        restMockMvc.perform(delete("$RESOURCE/{id}/delivery", id)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk)
                .andExpect(jsonPath("\$.status").value(DeliveryStatus.FAILED_DELIVERY_ATTEMPT.toString()))

        assertTotalMessagesAndReleaseThem()

        val eventContent = DummyProducerConnector.getMessageContent(FreightEvent::class)
        Assertions.assertEquals(FreightEventStatus.DELIVERY_FAILED, eventContent?.status)
    }

}