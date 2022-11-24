package boaentrega.gsl.transportation.configuration.config.evensourcing

import boaentrega.gsl.transportation.configuration.constants.EventSourcingBeanQualifiers
import boaentrega.gsl.transportation.support.eventsourcing.connectors.AbstractConnectorFactory
import boaentrega.gsl.transportation.support.eventsourcing.connectors.ConsumerConnector
import boaentrega.gsl.transportation.support.eventsourcing.connectors.DedicatedProducerConnector
import boaentrega.gsl.transportation.support.outbox.OutboxConnectorService
import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration


@Configuration
class OrderEventSourcingConfig {

    @Value("\${eventsourcing.messaging.order.event}")
    private lateinit var eventTarget: String

    @Value("\${eventsourcing.messaging.order.command}")
    private lateinit var commandTarget: String

    @Value("\${eventsourcing.messaging.order.document}")
    private lateinit var documentTarget: String

    @Bean(EventSourcingBeanQualifiers.ORDER_EVENT_CONSUMER)
    fun createEventConsumerConnector(
            connectorFactory: AbstractConnectorFactory): ConsumerConnector {
        return connectorFactory.createConsumer(eventTarget)
    }

    @Bean(EventSourcingBeanQualifiers.ORDER_COMMAND_PRODUCER)
    fun createCommandDedicatedProducer(connectorFactory: AbstractConnectorFactory,
                                       outboxConnectorService: OutboxConnectorService): DedicatedProducerConnector {
        return connectorFactory.createDedicatedProducer(outboxConnectorService, commandTarget)
    }

    @Bean(EventSourcingBeanQualifiers.ORDER_EVENT_PRODUCER)
    fun createEventDedicatedProducer(
            connectorFactory: AbstractConnectorFactory,
            outboxConnectorService: OutboxConnectorService): DedicatedProducerConnector {
        return connectorFactory.createDedicatedProducer(outboxConnectorService, eventTarget)
    }

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

    @Bean(EventSourcingBeanQualifiers.ORDER_COMMAND_CONSUMER)
    fun createCommandConsumerConnector(connectorFactory: AbstractConnectorFactory): ConsumerConnector {
        return connectorFactory.createConsumer(commandTarget)
    }
}