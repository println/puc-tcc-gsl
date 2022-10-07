package com.example.demo.configuration.config

import com.example.demo.configuration.constants.EventSourcingBeansConstants
import com.example.demo.support.eventsourcing.connectors.ConsumerConnector
import com.example.demo.support.eventsourcing.connectors.ProducerConnector
import com.example.demo.support.eventsourcing.connectors.amqp.AmqpConsumerConnector
import com.example.demo.support.eventsourcing.connectors.amqp.AmqpProducerConnector
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.databind.SerializationFeature
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule
import com.fasterxml.jackson.module.kotlin.KotlinFeature
import com.fasterxml.jackson.module.kotlin.KotlinModule
import com.fasterxml.jackson.module.kotlin.registerKotlinModule
import org.springframework.amqp.rabbit.connection.ConnectionFactory
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter
import org.springframework.amqp.support.converter.MessageConverter
import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration


@Configuration
class RabbitMqConfig {

    @Value("\${eventsourcing.queues.event.name}")
    private lateinit var eventQueue: String

    @Value("\${eventsourcing.queues.command.name}")
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

    @Bean(EventSourcingBeansConstants.CONTEXT_EVENT_PRODUCER)
    fun createEventProducerConnector(
            connectionFactory: ConnectionFactory,
            messageConverter: MessageConverter): ProducerConnector {
        return AmqpProducerConnector(eventQueue, connectionFactory, messageConverter)
    }

    @Bean(EventSourcingBeansConstants.CONTEXT_COMMAND_PRODUCER)
    fun createCommandProducerConnector(
            connectionFactory: ConnectionFactory,
            messageConverter: MessageConverter): ProducerConnector {
        return AmqpProducerConnector(commandQueue, connectionFactory, messageConverter)
    }

    @Bean(EventSourcingBeansConstants.CONTEXT_EVENT_CONSUMER)
    fun createEventConsumerConnector(connectionFactory: ConnectionFactory): ConsumerConnector {
        return AmqpConsumerConnector(eventQueue, connectionFactory)
    }

    @Bean(EventSourcingBeansConstants.CONTEXT_COMMAND_CONSUMER)
    fun createCommandConsumerConnector(connectionFactory: ConnectionFactory): ConsumerConnector {
        return AmqpConsumerConnector(commandQueue, connectionFactory)
    }
}