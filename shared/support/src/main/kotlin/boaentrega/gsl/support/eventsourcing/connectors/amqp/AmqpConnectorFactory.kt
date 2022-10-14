package boaentrega.gsl.support.eventsourcing.connectors.amqp

import boaentrega.gsl.support.eventsourcing.connectors.ConnectorFactory
import boaentrega.gsl.support.eventsourcing.connectors.ConsumerConnector
import boaentrega.gsl.support.eventsourcing.connectors.ProducerConnector
import org.springframework.amqp.rabbit.connection.ConnectionFactory
import org.springframework.amqp.support.converter.MessageConverter

class AmqpConnectorFactory(private val connectionFactory: ConnectionFactory,
                           private val messageConverter: MessageConverter) : ConnectorFactory {
    override fun createProducer(queue: String): ProducerConnector {
        return AmqpProducerConnector(queue, connectionFactory, messageConverter)
    }

    override fun createConsumer(queue: String): ConsumerConnector {
        return AmqpConsumerConnector(queue, connectionFactory)
    }
}