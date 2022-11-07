package boaentrega.gsl.order.support.eventsourcing.connectors.dummy

import boaentrega.gsl.order.support.eventsourcing.connectors.AbstractConnectorFactory
import boaentrega.gsl.order.support.eventsourcing.connectors.ConsumerConnector
import boaentrega.gsl.order.support.eventsourcing.connectors.ProducerConnector
import boaentrega.gsl.order.support.eventsourcing.scope.EventSourcingContext


class DummyConnectorFactory : AbstractConnectorFactory() {

    override fun createProducer(): ProducerConnector {
        return DummyProducerConnector()
    }

    override fun createConsumer(eventSourcingContext: EventSourcingContext, target: String): ConsumerConnector {
        return DummyConsumerConnector(eventSourcingContext, target)
    }
}