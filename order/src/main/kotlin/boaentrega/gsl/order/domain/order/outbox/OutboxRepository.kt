package boaentrega.gsl.order.domain.order.outbox

import org.springframework.stereotype.Repository

@Repository
interface OutboxRepository {
    fun saveMessage(outboxMessage: OutboxMessage)

    fun saveMessages(outboxMessages: List<OutboxMessage>)

    fun getMessages(numberOfMessages: Int): List<OutboxMessage>

    fun markAsPublished(messageIds: List<String>)
}