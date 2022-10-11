package boaentrega.gsl.order.domain.order.outbox

import java.time.OffsetDateTime
import java.util.*


class OutboxMessage {
    private val messageId: String
    private val messageType: String
    private val aggregateName: String
    private val aggregateId: String
    private val destination: String
    private val payload: String
    private val createdAt: OffsetDateTime
    private var isPublished = false

    internal constructor(messageType: String, aggregateName: String, aggregateId: String, destination: String, payload: String) {
        messageId = UUID.randomUUID().toString()
        this.messageType = messageType
        this.aggregateName = aggregateName
        this.aggregateId = aggregateId
        this.destination = destination
        this.payload = payload
        createdAt = OffsetDateTime.now()
    }

    private constructor(messageId: String, messageType: String, aggregateName: String, aggregateId: String,
                        destination: String, payload: String, createdAt: OffsetDateTime, isPublished: Boolean) {
        this.messageId = messageId
        this.messageType = messageType
        this.aggregateName = aggregateName
        this.aggregateId = aggregateId
        this.destination = destination
        this.payload = payload
        this.createdAt = createdAt
        this.isPublished = isPublished
    }

    companion object {
        fun rehydrate(messageId: String, messageType: String, aggregateName: String, aggregateId: String,
                      destination: String, payload: String, createdAt: OffsetDateTime,
                      isPublished: Boolean): OutboxMessage {
            return OutboxMessage(messageId, messageType, aggregateName, aggregateId, destination, payload, createdAt,
                    isPublished)
        }
    }
}
