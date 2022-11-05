package boaentrega.gsl.order.support.eventsourcing.connectors.amqp

import boaentrega.gsl.order.support.eventsourcing.connectors.AbstractConnectorFactory
import boaentrega.gsl.order.support.eventsourcing.connectors.ConsumerConnector
import boaentrega.gsl.order.support.eventsourcing.connectors.ProducerConnector
import org.springframework.amqp.rabbit.connection.ConnectionFactory
import org.springframework.amqp.support.converter.MessageConverter

class AmqpConnectorFactory(private val connectionFactory: ConnectionFactory,
                           private val messageConverter: MessageConverter) : AbstractConnectorFactory() {

    override fun createProducer(): ProducerConnector {
        return AmqpProducerConnector(connectionFactory, messageConverter)
    }

    override fun createConsumer(queue: String): ConsumerConnector {
        return AmqpConsumerConnector(queue, connectionFactory)
    }
}