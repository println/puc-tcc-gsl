package boaentrega.gsl.order.configuration.config

import boaentrega.gsl.order.configuration.constants.EventSourcingBeansConstants
import boaentrega.gsl.order.support.eventsourcing.connectors.AbstractConnectorFactory
import boaentrega.gsl.order.support.eventsourcing.connectors.ConsumerConnector
import boaentrega.gsl.order.support.eventsourcing.connectors.DedicatedProducerConnector
import boaentrega.gsl.order.support.eventsourcing.connectors.ProducerConnector
import boaentrega.gsl.order.support.eventsourcing.scope.EventSourcingContext
import boaentrega.gsl.order.support.outbox.OutboxConnectorService
import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration


@Configuration
class EventSourcingConfig {

    @Value("\${eventsourcing.messaging.freight.event}")
    private lateinit var freightEventTarget: String

    @Value("\${eventsourcing.messaging.freight.command}")
    private lateinit var freightCommandTarget: String

    @Value("\${eventsourcing.messaging.order.event}")
    private lateinit var orderEventTarget: String

    @Value("\${eventsourcing.messaging.order.command}")
    private lateinit var orderCommandTarget: String

    @Bean
    fun createProducerConnector(connectorFactory: AbstractConnectorFactory): ProducerConnector {
        return connectorFactory.createProducer()
    }

    @Bean(EventSourcingBeansConstants.FREIGHT_EVENT_PRODUCER)
    fun createEventDedicatedProducer(
            connectorFactory: AbstractConnectorFactory,
            outboxConnectorService: OutboxConnectorService): DedicatedProducerConnector {
        return connectorFactory.createDedicatedProducer(outboxConnectorService, freightEventTarget)
    }

    @Bean(EventSourcingBeansConstants.FREIGHT_COMMAND_PRODUCER)
    fun createCommandDedicatedProducer(connectorFactory: AbstractConnectorFactory,
                                       outboxConnectorService: OutboxConnectorService): DedicatedProducerConnector {
        return connectorFactory.createDedicatedProducer(outboxConnectorService, freightCommandTarget)
    }

    @Bean(EventSourcingBeansConstants.FREIGHT_EVENT_CONSUMER)
    fun createEventConsumerConnector(
            connectorFactory: AbstractConnectorFactory,
            eventSourcingContext: EventSourcingContext): ConsumerConnector {
        return connectorFactory.createConsumer(eventSourcingContext, freightEventTarget)
    }

    @Bean(EventSourcingBeansConstants.FREIGHT_COMMAND_CONSUMER)
    fun createCommandConsumerConnector(connectorFactory: AbstractConnectorFactory,
                                       eventSourcingContext: EventSourcingContext): ConsumerConnector {
        return connectorFactory.createConsumer(eventSourcingContext, freightCommandTarget)
    }

    @Bean(EventSourcingBeansConstants.ORDER_EVENT_CONSUMER)
    fun createOrderEventConsumerConnector(
            connectorFactory: AbstractConnectorFactory,
            eventSourcingContext: EventSourcingContext): ConsumerConnector {
        return connectorFactory.createConsumer(eventSourcingContext, orderEventTarget)
    }

    @Bean(EventSourcingBeansConstants.ORDER_COMMAND_PRODUCER)
    fun createOrderCommandDedicatedProducer(connectorFactory: AbstractConnectorFactory,
                                            outboxConnectorService: OutboxConnectorService): DedicatedProducerConnector {
        return connectorFactory.createDedicatedProducer(outboxConnectorService, orderCommandTarget)
    }

    @Bean(EventSourcingBeansConstants.ORDER_EVENT_PRODUCER)
    fun createOrderEventDedicatedProducer(
            connectorFactory: AbstractConnectorFactory,
            outboxConnectorService: OutboxConnectorService): DedicatedProducerConnector {
        return connectorFactory.createDedicatedProducer(outboxConnectorService, orderEventTarget)
    }

    @Bean(EventSourcingBeansConstants.ORDER_COMMAND_CONSUMER)
    fun createOrderCommandConsumerConnector(connectorFactory: AbstractConnectorFactory,
                                            eventSourcingContext: EventSourcingContext): ConsumerConnector {
        return connectorFactory.createConsumer(eventSourcingContext, orderCommandTarget)
    }

}