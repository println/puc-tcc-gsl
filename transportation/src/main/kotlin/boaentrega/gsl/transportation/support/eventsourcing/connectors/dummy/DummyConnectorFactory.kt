package boaentrega.gsl.transportation.support.eventsourcing.connectors.dummy

import boaentrega.gsl.transportation.support.eventsourcing.connectors.AbstractConnectorFactory
import boaentrega.gsl.transportation.support.eventsourcing.connectors.ConsumerConnector
import boaentrega.gsl.transportation.support.eventsourcing.connectors.ProducerConnector


class DummyConnectorFactory : AbstractConnectorFactory() {

    override fun createProducer(): ProducerConnector {
        return DummyProducerConnector()
    }

    override fun createConsumer(target: String): ConsumerConnector {
        return DummyConsumerConnector(target)
    }
}