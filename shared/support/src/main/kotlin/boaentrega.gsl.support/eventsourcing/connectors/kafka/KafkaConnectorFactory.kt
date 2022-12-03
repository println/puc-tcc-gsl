package boaentrega.gsl.support.eventsourcing.connectors.kafka

import boaentrega.gsl.support.eventsourcing.connectors.AbstractConnectorFactory
import boaentrega.gsl.support.eventsourcing.connectors.ConsumerConnector
import boaentrega.gsl.support.eventsourcing.connectors.ProducerConnector
import org.springframework.kafka.core.ConsumerFactory
import org.springframework.kafka.core.ProducerFactory

class KafkaConnectorFactory(
        private val consumerFactory: ConsumerFactory<String, String>,
        private val producerFactory: ProducerFactory<String, String>,
        private val groupId: String) : AbstractConnectorFactory() {

    override fun createProducer(): ProducerConnector {
        return KafkaProducerConnector(producerFactory)
    }

    override fun createConsumer(target: String): ConsumerConnector {
        return KafkaConsumerConnector(consumerFactory, target, groupId)
    }
}