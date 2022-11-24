package boaentrega.gsl.collection.support.outbox

import boaentrega.gsl.collection.support.eventsourcing.connectors.ProducerConnector
import boaentrega.gsl.collection.support.extensions.ClassExtensions.logger
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.util.*

@Service
class OutboxConnectorService(
        private val producerConnector: ProducerConnector,
        private val repository: OutboxRepository) : ProducerConnector {

    private val log = logger()

    @Transactional
    override fun publish(messageJson: String, target: String) {
        val outboxMessage = OutboxMessage(target, messageJson)
        log.info("Message is storing to [$target]: $messageJson")
        repository.save(outboxMessage)
        log.info("Message has been stored to [$target]: $messageJson")
    }

    @Transactional
    fun releaseMessages() {
        log.debug("Polling outbox for messages to publish")
        val outboxMessages: List<OutboxMessage> = repository.getTop10ByIsPublishedFalse()
        val successfullyPublishedIds: MutableList<UUID> = ArrayList()
        for (outboxMessage in outboxMessages) {
            producerConnector.publish(outboxMessage.payload, outboxMessage.destination)
            outboxMessage.messageId?.let { successfullyPublishedIds.add(it) }
        }
        if (successfullyPublishedIds.isNotEmpty()) {
            log.debug(String.format("Marking %d messages as successfully published", successfullyPublishedIds.size))
            repository.markAsPublished(successfullyPublishedIds.toList())
        }
    }
}