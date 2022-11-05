package boaentrega.gsl.order.configuration.config

import boaentrega.gsl.order.configuration.constants.EventSourcingBeansConstants
import boaentrega.gsl.order.support.eventsourcing.connectors.AbstractConnectorFactory
import boaentrega.gsl.order.support.eventsourcing.connectors.ConsumerConnector
import boaentrega.gsl.order.support.eventsourcing.connectors.DedicatedProducerConnector
import boaentrega.gsl.order.support.eventsourcing.connectors.ProducerConnector
import boaentrega.gsl.order.support.outbox.OutboxConnectorService
import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration


@Configuration
class EventSourcingConfig {

    @Value("\${eventsourcing.messaging.order.event}")
    private lateinit var orderEventTarget: String

    @Value("\${eventsourcing.messaging.order.command}")
    private lateinit var orderCommandTarget: String

    @Bean
    fun createProducerConnector(connectorFactory: AbstractConnectorFactory): ProducerConnector {
        return connectorFactory.createProducer()
    }

    @Bean(EventSourcingBeansConstants.CONTEXT_EVENT_PRODUCER)
    fun createEventDedicatedProducer(
            connectorFactory: AbstractConnectorFactory,
            outboxConnectorService: OutboxConnectorService): DedicatedProducerConnector {
        return connectorFactory.createDedicatedProducer(outboxConnectorService, orderEventTarget)
    }

    @Bean(EventSourcingBeansConstants.CONTEXT_COMMAND_PRODUCER)
    fun createCommandDedicatedProducer(connectorFactory: AbstractConnectorFactory,
                                       outboxConnectorService: OutboxConnectorService): DedicatedProducerConnector {
        return connectorFactory.createDedicatedProducer(outboxConnectorService, orderCommandTarget)
    }

    @Bean(EventSourcingBeansConstants.CONTEXT_EVENT_CONSUMER)
    fun createEventConsumerConnector(connectorFactory: AbstractConnectorFactory): ConsumerConnector {
        return connectorFactory.createConsumer(orderEventTarget)
    }

    @Bean(EventSourcingBeansConstants.CONTEXT_COMMAND_CONSUMER)
    fun createCommandConsumerConnector(connectorFactory: AbstractConnectorFactory): ConsumerConnector {
        return connectorFactory.createConsumer(orderCommandTarget)
    }
}