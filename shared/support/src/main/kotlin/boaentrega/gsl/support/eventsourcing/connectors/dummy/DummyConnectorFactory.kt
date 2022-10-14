package boaentrega.gsl.support.eventsourcing.connectors.dummy

import boaentrega.gsl.support.eventsourcing.connectors.ConnectorFactory
import boaentrega.gsl.support.eventsourcing.connectors.ConsumerConnector
import boaentrega.gsl.support.eventsourcing.connectors.ProducerConnector

class DummyConnectorFactory : ConnectorFactory {
    override fun createProducer(queue: String): ProducerConnector {
        return GenericProducerConnector(queue)
    }

    override fun createConsumer(queue: String): ConsumerConnector {
        return DummyConsumerConnector(queue)
    }
}