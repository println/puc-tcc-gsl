package boaentrega.gsl.order.domain.order.outbox

import boaentrega.gsl.order.domain.order.Order
import boaentrega.gsl.order.domain.order.eventsourcing.command.OrderCommandService
import boaentrega.gsl.order.domain.order.eventsourcing.event.OrderEventService
import org.springframework.stereotype.Service


@Service
class OutboxService(
        /*private val commandService: OrderCommandService,
        private val eventService: OrderEventService,
        private val repository: OutboxRepository*/) {

    fun saveMessage(order: Order){

    }


    fun publishMessages() {
//        log.debug("Polling outbox for messages to publish")
//        val outboxMessages: List<OutboxMessage> = repository.getMessages(outboxProperties.getBatchSize())
//        val successfullyPublishedIds: MutableList<String> = ArrayList()
//        for (outboxMessage in outboxMessages) {
//            try {
//                eventService.emit(outboxMessage)
//                successfullyPublishedIds.add(outboxMessage.getMessageId())
//            } catch (e: MessagePublishingException) {
//                log.info("Failed to publish message with message id " + outboxMessage.getMessageId())
//                //publish failed; don't mark id as published
//            }
//        }
//        if (!successfullyPublishedIds.isEmpty()) {
//            log.debug(String.format("Marking %d messages as successfully published", successfullyPublishedIds.size))
//            repository.markAsPublished(successfullyPublishedIds)
//        }
    }
}