package boaentrega.gsl.order.support.eventsourcing.connectors.dummy

import boaentrega.gsl.order.support.eventsourcing.connectors.AbstractConnectorFactory
import boaentrega.gsl.order.support.eventsourcing.connectors.ConsumerConnector
import boaentrega.gsl.order.support.eventsourcing.connectors.ProducerConnector


class DummyConnectorFactory : AbstractConnectorFactory() {

    override fun createProducer(): ProducerConnector {
        return DummyProducerConnector()
    }

    override fun createConsumer(target: String): ConsumerConnector {
        return DummyConsumerConnector(target)
    }
}