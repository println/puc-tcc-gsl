package boaentrega.gsl.collection.configuration.config

import boaentrega.gsl.collection.support.eventsourcing.connectors.AbstractConnectorFactory
import boaentrega.gsl.collection.support.eventsourcing.connectors.amqp.AmqpConnectorFactory
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.databind.SerializationFeature
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule
import com.fasterxml.jackson.module.kotlin.KotlinFeature
import com.fasterxml.jackson.module.kotlin.KotlinModule
import org.springframework.amqp.rabbit.connection.ConnectionFactory
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter
import org.springframework.amqp.support.converter.MessageConverter
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
@ConditionalOnProperty(prefix = "eventsourcing", name = ["platform"], havingValue = "rabbitmq")
class RabbitMqConnectorFactoryConfig {

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
    fun defineConnectorsFactory(connectionFactory: ConnectionFactory,
                                messageConverter: MessageConverter): AbstractConnectorFactory {
        return AmqpConnectorFactory(connectionFactory, messageConverter)
    }
}