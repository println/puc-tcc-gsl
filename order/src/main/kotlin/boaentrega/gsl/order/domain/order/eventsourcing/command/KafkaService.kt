package boaentrega.gsl.order.domain.order.eventsourcing.command

import org.springframework.kafka.annotation.KafkaListener
import org.springframework.stereotype.Service


//@Service
class KafkaService {
    @KafkaListener(topics = ["freight", "order"], groupId = "order")
    fun listenGroupFoo(message: String) {
        println("Received Message in group foo: $message")
    }
}