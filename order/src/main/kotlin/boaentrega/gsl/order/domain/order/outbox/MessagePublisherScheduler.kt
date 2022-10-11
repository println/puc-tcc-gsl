package boaentrega.gsl.order.domain.order.outbox

import org.springframework.scheduling.annotation.Scheduled
import org.springframework.stereotype.Component


@Component
class MessagePublisherScheduler(private val service: OutboxService) {
    @Scheduled(fixedDelayString = "\${outbox.storage.polling.frequencyInMillis:1000}")
    fun publishMessages() {
        service.publishMessages()
    }
}