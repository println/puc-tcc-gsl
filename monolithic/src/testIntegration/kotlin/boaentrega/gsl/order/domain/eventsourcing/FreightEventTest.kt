package boaentrega.gsl.order.domain.eventsourcing

import boaentrega.gsl.order.AbstractEventSourcingTest
import boaentrega.gsl.order.configuration.constants.EventSourcingBeanQualifiers
import boaentrega.gsl.order.configuration.constants.ResourcePaths
import boaentrega.gsl.order.configuration.constants.ServiceNames
import boaentrega.gsl.order.domain.freight.Freight
import boaentrega.gsl.order.domain.freight.FreightRepository
import boaentrega.gsl.order.domain.freight.FreightService
import boaentrega.gsl.order.domain.freight.FreightStatus
import boaentrega.gsl.order.domain.freight.web.FreightController
import boaentrega.gsl.support.eventsourcing.connectors.dummy.DummyConsumerConnector
import boaentrega.gsl.support.eventsourcing.messages.CommandMessage
import gsl.schemas.FreightEvent
import gsl.schemas.FreightEventStatus
import org.jeasy.random.EasyRandom
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.CsvSource
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.factory.annotation.Qualifier
import java.time.Instant
import java.util.*

class FreightEventTest : AbstractEventSourcingTest() {

    companion object {
        const val SOURCE = ServiceNames.FREIGHT
        const val RESOURCE = ResourcePaths.FREIGHT
    }

    @Autowired
    private lateinit var repository: FreightRepository

    @Autowired
    private lateinit var service: FreightService


    @Autowired
    @Qualifier(EventSourcingBeanQualifiers.FREIGHT_EVENT_CONSUMER)
    private lateinit var consumerConnector: DummyConsumerConnector


    override fun createResource(): Any {
        return FreightController(service)
    }

    val easyRandom = EasyRandom()
    var entity: Freight = easyRandom.nextObject(Freight::class.java)

    @AfterEach
    fun reset() {
        repository.deleteAll()
    }

    @BeforeEach
    fun setupEntity() {
        val orderId = UUID.randomUUID()
        val senderAddress = "senderAddress"
        val deliveryAddress = "deliveryAddress"
        val data = Freight(orderId, orderId, senderAddress, deliveryAddress, senderAddress)
        entity = repository.saveAndFlush(data)
    }

    @ParameterizedTest
    @CsvSource(
            "COLLECTION_STARTED, COLLECTION, COLLECTION_STARTED",
            "COLLECTION_PICKUP_OUT, COLLECTION, COLLECTION_PICKUP_OUT",
            "COLLECTION_STARTED, COLLECTION, COLLECTION_STARTED",
            "COLLECTION_PICKUP_OUT, COLLECTION, COLLECTION_PICKUP_OUT",
            "COLLECTION_PICKUP_TAKEN, COLLECTION, COLLECTION_PICKUP_TAKEN",
            "COLLECTION_PACKAGE_PREPARING, COLLECTION, COLLECTION_PACKAGE_PREPARING",
            "COLLECTION_PACKAGE_READY_TO_MOVE, COLLECTION, COLLECTION_PACKAGE_READY_TO_MOVE",
            "IN_TRANSIT_PACKAGE_STARTED, TRANSPORT, IN_TRANSIT_PACKAGE_STARTED",
            "IN_TRANSIT_PACKAGE_MOVING_ON_TO_NEXT_STORAGE, TRANSPORT, IN_TRANSIT_PACKAGE_MOVING_ON_TO_NEXT_STORAGE",
            "IN_TRANSIT_PACKAGE_RECEIVED_BY_STORAGE, TRANSPORT, IN_TRANSIT_PACKAGE_RECEIVED_BY_STORAGE",
            "IN_TRANSIT_PACKAGE_REACHED_FINAL_STORAGE, TRANSPORT, IN_TRANSIT_PACKAGE_REACHED_FINAL_STORAGE",
            "DELIVERY_STARTED, DELIVERY, DELIVERY_STARTED",
            "DELIVERY_OUT_FOR, DELIVERY, DELIVERY_OUT_FOR",
            "DELIVERY_FAILED, DELIVERY, DELIVERY_FAILED",
            "DELIVERY_PROCESS_RESTART, DELIVERY, DELIVERY_PROCESS_RESTART",
            "DELIVERY_SUCCESS, DELIVERY, DELIVERY_SUCCESS",
    )
    fun changeStatus(eventStatus: FreightEventStatus, source: String, expectedStatus: FreightStatus) {
        val event = Factory.createEvent(entity, eventStatus, source)
        val message = CommandMessage(event.trackId, event)

        repeat(3) {//check Duplication
            consumerConnector.consume(message)
        }

        val freight = checkAllFromApiAndGetFirst<Freight>(FreightCommandTest.RESOURCE)

        Assertions.assertEquals(entity.trackId, freight.trackId)
        Assertions.assertEquals(entity.id, freight.id)
        Assertions.assertEquals(entity.orderId, freight.orderId)
        Assertions.assertEquals(entity.senderAddress, freight.senderAddress)
        Assertions.assertEquals(entity.deliveryAddress, freight.deliveryAddress)
        Assertions.assertEquals(expectedStatus, freight.status)

        assertTotalMessagesAndReleaseThem(1)
        assertDocumentReleased(freight)
    }

    @Test
    fun deliverySuccessfully() {
        val event = Factory.createEvent(entity, FreightEventStatus.DELIVERY_SUCCESS, ServiceNames.DELIVERY)
        val message = CommandMessage(event.trackId, event)

        repeat(3) {//check Duplication
            consumerConnector.consume(message)
        }

        val freight = checkAllFromApiAndGetFirst<Freight>(FreightCommandTest.RESOURCE)

        Assertions.assertEquals(freight.deliveryAddress, freight.currentPosition)
    }


    /** List of events
    "CREATED", self - by command from order
    "COLLECTION_STARTED", collection
    "COLLECTION_PICKUP_OUT", collection
    "COLLECTION_PICKUP_TAKEN", collection
    "COLLECTION_PACKAGE_PREPARING", collection
    "COLLECTION_PACKAGE_READY_TO_MOVE", collection
    "IN_TRANSIT_PACKAGE_STARTED", transport
    "IN_TRANSIT_PACKAGE_MOVING_ON_TO_NEXT_STORAGE", transport
    "IN_TRANSIT_PACKAGE_RECEIVED_BY_STORAGE", transport
    "IN_TRANSIT_PACKAGE_REACHED_FINAL_STORAGE", transport
    "DELIVERY_STARTED", delivery
    "DELIVERY_OUT_FOR", delivery
    "DELIVERY_FAILED", delivery
    "DELIVERY_PROCESS_RESTART", delivery
    "DELIVERY_SUCCESS", delivery
    "CANCELING", self - by command CANCEL from ???
    "CANCELED", self??
    "FINISHED" self - by command FINISH from delivery
     */

    private object Factory {
        fun createEvent(entity: Freight, status: FreightEventStatus, source: String): FreightEvent {
            return FreightEvent(entity.trackId, entity.id, status, source, entity.currentPosition,
                    "Message: ${status.name}", Instant.now())
        }
    }

}