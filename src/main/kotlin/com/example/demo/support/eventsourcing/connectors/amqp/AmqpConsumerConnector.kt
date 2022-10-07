package com.example.demo.support.eventsourcing.connectors.amqp

import com.example.demo.support.eventsourcing.connectors.AbstractConsumerConnector
import com.example.demo.support.eventsourcing.messages.Message
import com.example.demo.support.extensions.ClassExtensions.toObject
import com.rabbitmq.client.Channel
import org.springframework.amqp.core.AcknowledgeMode
import org.springframework.amqp.rabbit.connection.ConnectionFactory
import org.springframework.amqp.rabbit.listener.SimpleMessageListenerContainer
import org.springframework.amqp.rabbit.listener.api.ChannelAwareMessageListener


class AmqpConsumerConnector(
        private val queueName: String,
        connectionFactory: ConnectionFactory) : ChannelAwareMessageListener, AbstractConsumerConnector() {

    private val listenerContainer: SimpleMessageListenerContainer = SimpleMessageListenerContainer(connectionFactory)

    init {
        listenerContainer.setQueueNames(queueName)
        listenerContainer.acknowledgeMode = AcknowledgeMode.AUTO
        listenerContainer.setMaxConcurrentConsumers(1)
        listenerContainer.setMessageListener(this)
    }

    override fun onMessage(message: org.springframework.amqp.core.Message, channel: Channel?) {
        val messageBody = String(message.body)
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
        return queueName
    }
}