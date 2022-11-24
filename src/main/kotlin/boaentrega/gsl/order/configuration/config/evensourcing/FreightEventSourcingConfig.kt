package boaentrega.gsl.order.configuration.config.evensourcing

import boaentrega.gsl.order.configuration.constants.EventSourcingBeanQualifiers
import boaentrega.gsl.order.support.eventsourcing.connectors.AbstractConnectorFactory
import boaentrega.gsl.order.support.eventsourcing.connectors.ConsumerConnector
import boaentrega.gsl.order.support.eventsourcing.connectors.DedicatedProducerConnector
import boaentrega.gsl.order.support.outbox.OutboxConnectorService
import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration


@Configuration
class FreightEventSourcingConfig {

    @Value("\${eventsourcing.messaging.freight.event}")
    private lateinit var eventTarget: String

    @Value("\${eventsourcing.messaging.freight.command}")
    private lateinit var commandTarget: String

    @Value("\${eventsourcing.messaging.freight.document}")
    private lateinit var documentTarget: String

    @Bean(EventSourcingBeanQualifiers.FREIGHT_EVENT_CONSUMER)
    fun createEventConsumerConnector(
            connectorFactory: AbstractConnectorFactory): ConsumerConnector {
        return connectorFactory.createConsumer(eventTarget)
    }

    @Bean(EventSourcingBeanQualifiers.FREIGHT_EVENT_PRODUCER)
    fun createEventDedicatedProducer(
            connectorFactory: AbstractConnectorFactory,
            outboxConnectorService: OutboxConnectorService): DedicatedProducerConnector {
        return connectorFactory.createDedicatedProducer(outboxConnectorService, eventTarget)
    }

    @Bean(EventSourcingBeanQualifiers.FREIGHT_COMMAND_CONSUMER)
    fun createCommandConsumerConnector(connectorFactory: AbstractConnectorFactory): ConsumerConnector {
        return connectorFactory.createConsumer(commandTarget)
    }

    @Bean(EventSourcingBeanQualifiers.FREIGHT_COMMAND_PRODUCER)
    fun createCommandDedicatedProducer(connectorFactory: AbstractConnectorFactory,
                                       outboxConnectorService: OutboxConnectorService): DedicatedProducerConnector {
        return connectorFactory.createDedicatedProducer(outboxConnectorService, commandTarget)
    }

    @Bean(EventSourcingBeanQualifiers.FREIGHT_DOCUMENT_CONSUMER)
    fun createDocumentConsumerConnector(connectorFactory: AbstractConnectorFactory): ConsumerConnector {
        return connectorFactory.createConsumer(documentTarget)
    }

    @Bean(EventSourcingBeanQualifiers.FREIGHT_DOCUMENT_PRODUCER)
    fun createDocumentDedicatedProducer(connectorFactory: AbstractConnectorFactory,
                                       outboxConnectorService: OutboxConnectorService): DedicatedProducerConnector {
        return connectorFactory.createDedicatedProducer(outboxConnectorService, documentTarget)
    }


}