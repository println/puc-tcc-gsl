package boaentrega.gsl.support.eventsourcing.connectors.kafka

import boaentrega.gsl.support.eventsourcing.connectors.ProducerConnector
import boaentrega.gsl.support.eventsourcing.messages.Message
import boaentrega.gsl.support.extensions.ClassExtensions.logger
import boaentrega.gsl.support.extensions.ClassExtensions.toJsonString
import org.springframework.kafka.core.KafkaTemplate
import org.springframework.kafka.core.ProducerFactory
import org.springframework.kafka.support.converter.StringJsonMessageConverter


class KafkaProducerConnector(
        producerFactory: ProducerFactory<String, String>,
        private val topic: String,
        private val groupId: String = ""
) : ProducerConnector {

    private val logger = logger()

    private val template: KafkaTemplate<String, String> = KafkaTemplate(producerFactory)

    init {
        logger.info("Creating Producer Connector: $topic")
        template.messageConverter = StringJsonMessageConverter()
    }

    override fun publish(message: Message) {
        template.send(topic, message.toJsonString())
    }

    override fun getId(): String {
        return topic
    }
}