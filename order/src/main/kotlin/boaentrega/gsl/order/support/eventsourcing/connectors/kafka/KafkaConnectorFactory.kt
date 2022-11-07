package boaentrega.gsl.order.support.eventsourcing.connectors.kafka

import boaentrega.gsl.order.support.eventsourcing.connectors.AbstractConnectorFactory
import boaentrega.gsl.order.support.eventsourcing.connectors.ConsumerConnector
import boaentrega.gsl.order.support.eventsourcing.connectors.ProducerConnector
import boaentrega.gsl.order.support.eventsourcing.scope.EventSourcingContext
import org.springframework.kafka.core.ConsumerFactory
import org.springframework.kafka.core.ProducerFactory

class KafkaConnectorFactory(
        private val consumerFactory: ConsumerFactory<String, String>,
        private val producerFactory: ProducerFactory<String, String>,
        private val groupId: String) : AbstractConnectorFactory() {

    override fun createProducer(): ProducerConnector {
        return KafkaProducerConnector(producerFactory)
    }

    override fun createConsumer(eventSourcingContext: EventSourcingContext, target: String): ConsumerConnector {
        return KafkaConsumerConnector(eventSourcingContext, consumerFactory, target, groupId)
    }
}