package boaentrega.gsl.transportation.support.outbox

import org.springframework.context.annotation.Profile
import org.springframework.scheduling.annotation.Scheduled
import org.springframework.stereotype.Component

@Profile("!test")
@Component
class MessagePublisherScheduler(private val connectorService: OutboxConnectorService) {
    @Scheduled(fixedDelayString = "\${outbox.storage.polling.frequencyInMillis:1000}")
    fun publishMessages() {
        connectorService.releaseMessages()
    }
}