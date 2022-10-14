package boaentrega.gsl.order.configuration.config

import boaentrega.gsl.order.configuration.constants.EventSourcingBeansConstants
import boaentrega.gsl.support.eventsourcing.connectors.ConnectorFactory
import boaentrega.gsl.support.eventsourcing.connectors.ConsumerConnector
import boaentrega.gsl.support.eventsourcing.connectors.ProducerConnector
import boaentrega.gsl.support.eventsourcing.connectors.amqp.AmqpConnectorFactory
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.databind.SerializationFeature
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule
import com.fasterxml.jackson.module.kotlin.KotlinFeature
import com.fasterxml.jackson.module.kotlin.KotlinModule
import org.springframework.amqp.rabbit.connection.ConnectionFactory
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter
import org.springframework.amqp.support.converter.MessageConverter
import org.springframework.beans.factory.annotation.Value
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration


@Configuration
@ConditionalOnProperty(prefix = "eventsourcing", name = ["platform"], havingValue = "rabbitmq")
class RabbitMqConfig {

    @Value("\${eventsourcing.amqp.queues.event.name}")
    private lateinit var eventQueue: String

    @Value("\${eventsourcing.amqp.queues.command.name}")
    private lateinit var commandQueue: String

    @Bean
    fun messageConverter(objectMapper: ObjectMapper): MessageConverter? {
        objectMapper.registerModule(JavaTimeModule())
        objectMapper.registerModule(KotlinModule.Builder()
                .withReflectionCacheSize(512)
                .configure(KotlinFeature.NullToEmptyCollection, false)
                .configure(KotlinFeature.NullToEmptyMap, false)
                .configure(KotlinFeature.NullIsSameAsDefault, false)
                .configure(KotlinFeature.SingletonSupport, false)
                .configure(KotlinFeature.StrictNullChecks, false)
                .build())
        objectMapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS)
        return Jackson2JsonMessageConverter(objectMapper)
    }

    @Bean
    fun defineConnectorsFactory( connectionFactory: ConnectionFactory,
                               messageConverter: MessageConverter):ConnectorFactory{
        return AmqpConnectorFactory(connectionFactory, messageConverter)
    }

    @Bean(EventSourcingBeansConstants.CONTEXT_EVENT_PRODUCER)
    fun createEventProducerConnector(connectorFactory: ConnectorFactory): ProducerConnector {
        return connectorFactory.createProducer(eventQueue)
    }

    @Bean(EventSourcingBeansConstants.CONTEXT_COMMAND_PRODUCER)
    fun createCommandProducerConnector(connectorFactory: ConnectorFactory): ProducerConnector {
        return connectorFactory.createProducer(commandQueue)
    }

    @Bean(EventSourcingBeansConstants.CONTEXT_EVENT_CONSUMER)
    fun createEventConsumerConnector(connectorFactory: ConnectorFactory): ConsumerConnector {
        return connectorFactory.createConsumer(eventQueue)
    }

    @Bean(EventSourcingBeansConstants.CONTEXT_COMMAND_CONSUMER)
    fun createCommandConsumerConnector(connectorFactory: ConnectorFactory): ConsumerConnector {
        return connectorFactory.createConsumer(commandQueue)
    }
}