package boaentrega.gsl.order.support.eventsourcing.connectors.amqp


import boaentrega.gsl.order.support.eventsourcing.connectors.AbstractProducerConnector
import boaentrega.gsl.order.support.extensions.ClassExtensions.logger
import org.springframework.amqp.rabbit.connection.ConnectionFactory
import org.springframework.amqp.rabbit.core.RabbitTemplate
import org.springframework.amqp.support.converter.MessageConverter

class AmqpProducerConnector(
        connectionFactory: ConnectionFactory,
        messageConverter: MessageConverter
) : AbstractProducerConnector() {

    private val logger = logger()

    private val template: RabbitTemplate = RabbitTemplate(connectionFactory)

    init{
        logger.info("Creating AMQP Producer Connector")
        template.messageConverter = messageConverter
    }

    override fun send(messageJson: String, target: String) {
        template.convertAndSend(target, messageJson)
    }
}