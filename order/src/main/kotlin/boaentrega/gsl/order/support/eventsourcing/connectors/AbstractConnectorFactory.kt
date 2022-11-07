package boaentrega.gsl.order.support.eventsourcing.connectors

import boaentrega.gsl.order.support.eventsourcing.scope.EventSourcingContext
import boaentrega.gsl.order.support.outbox.OutboxConnectorService

abstract class AbstractConnectorFactory {

    abstract fun createProducer(): ProducerConnector

    abstract fun createConsumer(eventSourcingContext: EventSourcingContext, target: String): ConsumerConnector

    fun createDedicatedProducer(outboxConnectorService: OutboxConnectorService, target: String): DedicatedProducerConnector {
        return DedicatedProducerConnector(outboxConnectorService, target)
    }
}