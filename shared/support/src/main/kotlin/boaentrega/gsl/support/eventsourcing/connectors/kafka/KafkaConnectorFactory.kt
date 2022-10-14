package boaentrega.gsl.support.eventsourcing.connectors.kafka

import boaentrega.gsl.support.eventsourcing.connectors.ConnectorFactory
import boaentrega.gsl.support.eventsourcing.connectors.ConsumerConnector
import boaentrega.gsl.support.eventsourcing.connectors.ProducerConnector
import org.springframework.kafka.core.ConsumerFactory
import org.springframework.kafka.core.ProducerFactory

class KafkaConnectorFactory(
        private val consumerFactory: ConsumerFactory<String, String>,
        private val producerFactory: ProducerFactory<String, String>) : ConnectorFactory {

    override fun createProducer(topic: String): ProducerConnector {
        return KafkaProducerConnector(producerFactory,topic)
    }

    override fun createConsumer(topic: String): ConsumerConnector {
        return KafkaConsumerConnector(consumerFactory, topic)
    }
}