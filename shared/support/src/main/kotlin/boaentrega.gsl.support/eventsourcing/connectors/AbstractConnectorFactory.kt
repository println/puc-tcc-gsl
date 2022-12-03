package boaentrega.gsl.support.eventsourcing.connectors

import boaentrega.gsl.support.outbox.OutboxConnectorService

abstract class AbstractConnectorFactory {

    abstract fun createProducer(): ProducerConnector

    abstract fun createConsumer(target: String): ConsumerConnector

    fun createDedicatedProducer(outboxConnectorService: OutboxConnectorService, target: String): DedicatedProducerConnector {
        return DedicatedProducerConnector(outboxConnectorService, target)
    }
}