package boaentrega.gsl.freight.configuration.config.evensourcing

import boaentrega.gsl.freight.configuration.constants.EventSourcingBeanQualifiers
import boaentrega.gsl.freight.support.eventsourcing.connectors.AbstractConnectorFactory
import boaentrega.gsl.freight.support.eventsourcing.connectors.ConsumerConnector
import boaentrega.gsl.freight.support.eventsourcing.connectors.DedicatedProducerConnector
import boaentrega.gsl.freight.support.outbox.OutboxConnectorService
import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration


@Configuration
class OrderEventSourcingConfig {

    @Value("\${eventsourcing.messaging.order.document}")
    private lateinit var documentTarget: String

    @Bean(EventSourcingBeanQualifiers.ORDER_DOCUMENT_PRODUCER)
    fun createDocumentDedicatedProducer(
            connectorFactory: AbstractConnectorFactory,
            outboxConnectorService: OutboxConnectorService): DedicatedProducerConnector {
        return connectorFactory.createDedicatedProducer(outboxConnectorService, documentTarget)
    }

    @Bean(EventSourcingBeanQualifiers.ORDER_DOCUMENT_CONSUMER)
    fun createDocumentConsumerConnector(connectorFactory: AbstractConnectorFactory): ConsumerConnector {
        return connectorFactory.createConsumer(documentTarget)
    }

}