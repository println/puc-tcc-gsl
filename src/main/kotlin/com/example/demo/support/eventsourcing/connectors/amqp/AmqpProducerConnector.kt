package com.example.demo.support.eventsourcing.connectors.amqp

import com.example.demo.support.eventsourcing.connectors.ProducerConnector
import com.example.demo.support.eventsourcing.messages.Message
import com.example.demo.support.extensions.ClassExtensions.logger
import org.springframework.amqp.rabbit.connection.ConnectionFactory
import org.springframework.amqp.rabbit.core.RabbitTemplate
import org.springframework.amqp.support.converter.MessageConverter

class AmqpProducerConnector(
        private val queueName: String,
        connectionFactory: ConnectionFactory,
        messageConverter: MessageConverter
) : ProducerConnector {

    private val logger = logger()

    private val template: RabbitTemplate = RabbitTemplate(connectionFactory)

    init{
        logger.info("Creating Producer Connector: $queueName")
        template.messageConverter = messageConverter
    }

    override fun publish(message: Message) {
        template.convertAndSend(queueName, message)
    }
}