package boaentrega.gsl.delivery.support.eventsourcing.messages

import boaentrega.gsl.delivery.support.extensions.ClassExtensions.toJsonString
import boaentrega.gsl.delivery.support.extensions.ClassExtensions.toObject
import gsl.schemas.FreightEvent
import gsl.schemas.FreightEventStatus
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import java.time.Instant
import java.time.LocalDate
import java.util.*

internal class MessageTest {

    @Test
    fun serializeWithAvroData() {
        val avroEventData = FreightEvent(
                UUID.randomUUID(),
                UUID.randomUUID(),
                FreightEventStatus.COLLECTION_PICKUP_OUT,
                "source",
                "currentPosition",
                "message",
                Instant.now())

        val deserializedAvroEventData = testMessageSerialization(avroEventData)

        assertEquals(avroEventData.trackId, deserializedAvroEventData.trackId)
        assertEquals(avroEventData.freightId, deserializedAvroEventData.freightId)
        assertEquals(avroEventData.status, deserializedAvroEventData.status)
        assertEquals(avroEventData.source, deserializedAvroEventData.source)
        assertEquals(avroEventData.message, deserializedAvroEventData.message)
        assertEquals(avroEventData.date, deserializedAvroEventData.date)
    }

    @Test
    fun serializeWithCustomData() {
        val eventData = CustomEventData(
                UUID.randomUUID(),
                CustomEventDataStatus.ACCEPTED,
                "teste",
                "teste",
                LocalDate.now())

        val deserializedEventData = testMessageSerialization(eventData)

        assertEquals(eventData.trackId, deserializedEventData.trackId)
        assertEquals(eventData.status, deserializedEventData.status)
        assertEquals(eventData.source, deserializedEventData.source)
        assertEquals(eventData.message, deserializedEventData.message)
        assertEquals(eventData.date, deserializedEventData.date)
    }

    private inline fun <reified T : Any> testMessageSerialization(content: T): T {
        val message = Message(UUID.randomUUID(), MessageType.EVENT, content)
        val messageJson = message.toJsonString()

        val deserializedMessage = messageJson.toObject<Message>()

        assertEquals(message.trackId, deserializedMessage.trackId)
        assertEquals(message.type, deserializedMessage.type)
        assertEquals(message.content, deserializedMessage.content)

        return message.content.toObject()
    }

    private data class CustomEventData(
            val trackId: UUID,
            val status: CustomEventDataStatus,
            val source: String,
            val message: String,
            val date: LocalDate
    )

    private enum class CustomEventDataStatus {
        CREATED, ACCEPTED, REFUSED
    }
}