package boaentrega.gsl.order.configuration.config

import boaentrega.gsl.order.configuration.constants.EventSourcingBeansConstants
import boaentrega.gsl.support.eventsourcing.connectors.ConnectorFactory
import boaentrega.gsl.support.eventsourcing.connectors.ConsumerConnector
import boaentrega.gsl.support.eventsourcing.connectors.ProducerConnector
import boaentrega.gsl.support.eventsourcing.connectors.kafka.KafkaConnectorFactory
import org.springframework.beans.factory.annotation.Value
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.kafka.core.ConsumerFactory
import org.springframework.kafka.core.DefaultKafkaConsumerFactory
import org.springframework.kafka.core.DefaultKafkaProducerFactory
import org.springframework.kafka.core.ProducerFactory


@Configuration
@ConditionalOnProperty(prefix = "eventsourcing", name = ["platform"], havingValue = "kafka")
class KafkaConfig {
    @Value("\${eventsourcing.kafka.topics.order}")
    private lateinit var order: String

    @Value("\${eventsourcing.kafka.topics.freight}")
    private lateinit var freight: String

    @Value(value = "\${spring.kafka.bootstrap-servers}")
    private lateinit var bootstrapAddress: String

    fun createProps(): Map<String, String> {
        val props = mutableMapOf<String, String>()
        props["bootstrap.servers"] = bootstrapAddress
        props["group.id"] = "order"
        props["enable.auto.commit"] = "true"
        props["auto.commit.interval.ms"] = "1000"
        props["session.timeout.ms"] = "30000"
        props["key.serializer"] = "org.apache.kafka.common.serialization.StringSerializer"
        props["value.serializer"] = "org.apache.kafka.common.serialization.StringSerializer"
        props["key.deserializer"] = "org.apache.kafka.common.serialization.StringDeserializer"
        props["value.deserializer"] = "org.apache.kafka.common.serialization.StringDeserializer"
        props["partition.assignment.strategy"] = "range"
        return props
    }

    @Bean
    fun defineConnectorsFactory(producerFactory: ProducerFactory<String, String>): ConnectorFactory {
        val props = createProps()
        val consumerConnector: ConsumerFactory<String, String> = DefaultKafkaConsumerFactory(props)
        val producerConnector: ProducerFactory<String, String> = DefaultKafkaProducerFactory(props)
        return KafkaConnectorFactory(consumerConnector, producerConnector)
    }

    @Bean(EventSourcingBeansConstants.CONTEXT_EVENT_PRODUCER)
    fun createEventProducerConnector(connectorFactory: ConnectorFactory): ProducerConnector {
        return connectorFactory.createProducer(order)
    }

    @Bean(EventSourcingBeansConstants.CONTEXT_COMMAND_PRODUCER)
    fun createCommandProducerConnector(connectorFactory: ConnectorFactory): ProducerConnector {
        return connectorFactory.createProducer(freight)
    }

    @Bean(EventSourcingBeansConstants.CONTEXT_EVENT_CONSUMER)
    fun createEventConsumerConnector(connectorFactory: ConnectorFactory): ConsumerConnector {
        return connectorFactory.createConsumer(order)
    }

    @Bean(EventSourcingBeansConstants.CONTEXT_COMMAND_CONSUMER)
    fun createCommandConsumerConnector(connectorFactory: ConnectorFactory): ConsumerConnector {
        return connectorFactory.createConsumer(freight)
    }
}