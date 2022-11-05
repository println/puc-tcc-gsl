package boaentrega.gsl.order.configuration.config

import boaentrega.gsl.order.support.eventsourcing.connectors.AbstractConnectorFactory
import boaentrega.gsl.order.support.eventsourcing.connectors.kafka.KafkaConnectorFactory
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
class KafkaConnectorFactoryConfig {

    @Value(value = "\${spring.kafka.bootstrap-servers}")
    private lateinit var bootstrapAddress: String

    @Value(value = "\${spring.kafka.consumer.group-id}")
    private lateinit var groupId: String

    fun createProps(): Map<String, String> {
        val props = mutableMapOf<String, String>()
        props["bootstrap.servers"] = bootstrapAddress
        props["group.id"] = groupId
        props["enable.auto.commit"] = "true"
        props["auto.commit.interval.ms"] = "1000"
        props["session.timeout.ms"] = "30000"
        props["key.serializer"] = "org.apache.kafka.common.serialization.StringSerializer"
        props["value.serializer"] = "org.apache.kafka.common.serialization.StringSerializer"
        props["key.deserializer"] = "org.apache.kafka.common.serialization.StringDeserializer"
        props["value.deserializer"] = "org.apache.kafka.common.serialization.StringDeserializer"
        return props
    }

    @Bean
    fun defineConnectorsFactory(producerFactory: ProducerFactory<String, String>): AbstractConnectorFactory {
        val props = createProps()
        val consumerConnector: ConsumerFactory<String, String> = DefaultKafkaConsumerFactory(props)
        val producerConnector: ProducerFactory<String, String> = DefaultKafkaProducerFactory(props)
        return KafkaConnectorFactory(consumerConnector, producerConnector, groupId)
    }
}