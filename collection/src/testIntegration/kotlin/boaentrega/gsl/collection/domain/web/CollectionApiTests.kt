package boaentrega.gsl.collection.domain.web

import boaentrega.gsl.collection.AbstractWebTest
import boaentrega.gsl.collection.configuration.constants.ResourcePaths
import boaentrega.gsl.collection.domain.pickup.PickupRequest
import boaentrega.gsl.collection.domain.pickup.PickupRequestRepository
import boaentrega.gsl.collection.domain.pickup.PickupRequestStatus
import boaentrega.gsl.collection.domain.pickup.web.PickupRequestController
import boaentrega.gsl.collection.domain.pickup.web.PickupRequestWebService
import boaentrega.gsl.collection.support.eventsourcing.connectors.dummy.DummyProducerConnector
import boaentrega.gsl.collection.support.extensions.ClassExtensions.toJsonString
import boaentrega.gsl.collection.support.extensions.ClassExtensions.toObject
import gsl.schemas.FreightEvent
import gsl.schemas.FreightEventStatus
import gsl.schemas.FreightMovePackageCommand
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.MediaType
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.status

internal class CollectionApiTests : AbstractWebTest<PickupRequest>() {

    companion object {
        const val RESOURCE = ResourcePaths.COLLECTION
    }

    @Autowired
    private lateinit var repository: PickupRequestRepository

    @Autowired
    private lateinit var service: PickupRequestWebService

    override fun createResource(): Any {
        return PickupRequestController(service)
    }

    override fun getRepository() = repository
    override fun getEntityType() = PickupRequest::class.java
    override fun preProcessing(data: PickupRequest) {
        data.status = PickupRequestStatus.WAITING
    }

    override fun getResource() = RESOURCE

    @AfterEach
    fun reset() {
        repository.deleteAll()
    }

    @Test
    fun checkStatusById() {
        val id = entities.first().id
        restMockMvc.perform(get("$RESOURCE{id}", id))
                .andExpect(jsonPath("\$.status").value(PickupRequestStatus.WAITING.toString()))
    }

    @Test
    fun markAsOutToPickupTheProduct() {
        val id = entities.first().id
        val contentMap = mapOf("employee" to "fulano de tal")
        restMockMvc.perform(put("$RESOURCE{id}/pickup", id)
                .contentType(MediaType.APPLICATION_JSON)
                .content(contentMap.toJsonString())
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk)
                .andExpect(
                        jsonPath("\$.status").value(PickupRequestStatus.PICKUP_PROCESS.toString()))

        assertTotalMessagesAndReleaseThem()

        val eventContent = DummyProducerConnector.getMessageContent(FreightEvent::class)
        Assertions.assertEquals(FreightEventStatus.COLLECTION_PICKUP_OUT, eventContent?.status)
    }

    @Test
    fun markAsTaken() {
        reloadData { it.status = PickupRequestStatus.PICKUP_PROCESS }
        val id = entities.first().id
        val contentMap = mapOf("address" to "some forest")
        restMockMvc.perform(put("$RESOURCE{id}/taken", id)
                .contentType(MediaType.APPLICATION_JSON)
                .content(contentMap.toJsonString())
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk)
                .andExpect(
                        jsonPath("\$.status").value(PickupRequestStatus.TAKEN.toString()))
                .andExpect(
                        jsonPath("\$.currentPosition").value(contentMap["address"]))

        assertTotalMessagesAndReleaseThem()

        val eventContent = DummyProducerConnector.getMessageContent(FreightEvent::class)
        Assertions.assertEquals(FreightEventStatus.COLLECTION_PICKUP_TAKEN, eventContent?.status)
    }

    @Test
    fun markAsOnPackaging() {
        reloadData { it.status = PickupRequestStatus.TAKEN }
        val id = entities.first().id
        val contentMap = mapOf("employee" to "fulano de tal")
        restMockMvc.perform(put("$RESOURCE{id}/packaging", id)
                .contentType(MediaType.APPLICATION_JSON)
                .content(contentMap.toJsonString())
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk)
                .andExpect(
                        jsonPath("\$.status").value(PickupRequestStatus.ON_PACKAGING.toString()))

        assertTotalMessagesAndReleaseThem()

        val eventContent = DummyProducerConnector.getMessageContent(FreightEvent::class)
        Assertions.assertEquals(FreightEventStatus.COLLECTION_PACKAGE_PREPARING, eventContent?.status)
    }

    @Test
    fun markAsReadyToStartDelivery() {
        reloadData { it.status = PickupRequestStatus.ON_PACKAGING }
        val id = entities.first().id
        val contentMap = mapOf("address" to "some desert")
        val result = restMockMvc.perform(put("$RESOURCE{id}/ready", id)
                .contentType(MediaType.APPLICATION_JSON)
                .content(contentMap.toJsonString())
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk)
                .andExpect(
                        jsonPath("\$.status").value(PickupRequestStatus.FINISHED.toString()))
                .andExpect(
                        jsonPath("\$.currentPosition").value(contentMap["address"]))
                .andReturn()

        val pickupRequest = result.response.contentAsString.toObject<PickupRequest>()

        assertTotalMessagesAndReleaseThem(2)

        val eventContent = DummyProducerConnector.getMessageContent(FreightEvent::class)
        Assertions.assertEquals(FreightEventStatus.COLLECTION_PACKAGE_READY_TO_MOVE, eventContent?.status)

        val commandContent = DummyProducerConnector.getMessageContent(FreightMovePackageCommand::class, 1)
        Assertions.assertEquals(pickupRequest.trackId, commandContent?.trackId)
        Assertions.assertEquals(pickupRequest.orderId, commandContent?.orderId)
        Assertions.assertEquals(pickupRequest.freightId, commandContent?.freightId)
        Assertions.assertEquals(pickupRequest.currentPosition, commandContent?.currentPosition)
        Assertions.assertEquals(pickupRequest.deliveryAddress, commandContent?.deliveryAddress)
    }

}