package boaentrega.gsl.collection.support.eventsourcing.connectors

import boaentrega.gsl.collection.support.outbox.OutboxConnectorService

abstract class AbstractConnectorFactory {

    abstract fun createProducer(): ProducerConnector

    abstract fun createConsumer(target: String): ConsumerConnector

    fun createDedicatedProducer(outboxConnectorService: OutboxConnectorService, target: String): DedicatedProducerConnector {
        return DedicatedProducerConnector(outboxConnectorService, target)
    }
}