package boaentrega.gsl.support.eventsourcing.connectors.dummy

import boaentrega.gsl.support.eventsourcing.connectors.AbstractConnectorFactory
import boaentrega.gsl.support.eventsourcing.connectors.ConsumerConnector
import boaentrega.gsl.support.eventsourcing.connectors.ProducerConnector


class DummyConnectorFactory : AbstractConnectorFactory() {

    override fun createProducer(): ProducerConnector {
        return DummyProducerConnector()
    }

    override fun createConsumer(target: String): ConsumerConnector {
        return DummyConsumerConnector(target)
    }
}