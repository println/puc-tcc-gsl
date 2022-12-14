package boaentrega.gsl.transportation.support.eventsourcing.connectors.kafka

import boaentrega.gsl.transportation.support.eventsourcing.connectors.AbstractProducerConnector
import boaentrega.gsl.transportation.support.extensions.ClassExtensions.logger
import org.springframework.kafka.core.KafkaTemplate
import org.springframework.kafka.core.ProducerFactory
import org.springframework.kafka.support.converter.StringJsonMessageConverter


class KafkaProducerConnector(
        producerFactory: ProducerFactory<String, String>
) : AbstractProducerConnector() {

    private val logger = logger()

    private val template: KafkaTemplate<String, String> = KafkaTemplate(producerFactory)

    init {
        logger.info("Creating Kafka Producer Connector")
        template.messageConverter = StringJsonMessageConverter()
    }

    override fun send(messageJson: String, target: String) {
        template.send(target, messageJson)
    }
}