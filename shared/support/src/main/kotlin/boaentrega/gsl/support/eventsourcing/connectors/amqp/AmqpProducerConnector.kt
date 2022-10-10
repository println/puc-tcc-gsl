package boaentrega.gsl.support.eventsourcing.connectors.amqp

import boaentrega.gsl.support.eventsourcing.connectors.ProducerConnector
import boaentrega.gsl.support.eventsourcing.messages.Message
import boaentrega.gsl.support.extensions.ClassExtensions.logger
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