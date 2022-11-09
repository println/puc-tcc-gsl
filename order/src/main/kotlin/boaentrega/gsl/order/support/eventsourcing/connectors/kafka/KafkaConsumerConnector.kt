package boaentrega.gsl.order.support.eventsourcing.connectors.kafka

import boaentrega.gsl.order.support.eventsourcing.connectors.AbstractConsumerConnector
import boaentrega.gsl.order.support.eventsourcing.messages.Message
import boaentrega.gsl.order.support.extensions.ClassExtensions.logger
import boaentrega.gsl.order.support.extensions.ClassExtensions.toObject
import org.apache.kafka.clients.consumer.ConsumerRecord
import org.springframework.kafka.core.ConsumerFactory
import org.springframework.kafka.listener.ContainerProperties
import org.springframework.kafka.listener.KafkaMessageListenerContainer
import org.springframework.kafka.listener.MessageListener
import org.springframework.kafka.support.TopicPartitionOffset


class KafkaConsumerConnector(
        consumerFactory: ConsumerFactory<String, String>,
        private val topic: String,
        private val groupId: String
): MessageListener<String, String>, AbstractConsumerConnector() {

    private val logger = logger()

    private val listenerContainer: KafkaMessageListenerContainer<String, String>

    init {
        logger.info("Creating Kafka Consumer Connector: $topic")
        val cProps = ContainerProperties(TopicPartitionOffset(topic,  0,  0L))
        listenerContainer = KafkaMessageListenerContainer<String, String>(consumerFactory, cProps)
        listenerContainer.setupMessageListener(this)
    }

    override fun onMessage(data: ConsumerRecord<String, String>) {
        val messageBody = data.value()
        val obj = messageBody.toObject(Message::class.java)
        consume(obj)
    }

    override fun startConsumer() {
        listenerContainer.start()
    }

    override fun stopConsumer() {
        listenerContainer.stop()
    }

    override fun getId(): String {
        return topic
    }
}