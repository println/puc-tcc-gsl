package boaentrega.gsl.collection.support.eventsourcing.connectors.amqp

import boaentrega.gsl.collection.support.eventsourcing.connectors.AbstractConnectorFactory
import boaentrega.gsl.collection.support.eventsourcing.connectors.ConsumerConnector
import boaentrega.gsl.collection.support.eventsourcing.connectors.ProducerConnector
import org.springframework.amqp.rabbit.connection.ConnectionFactory
import org.springframework.amqp.support.converter.MessageConverter

class AmqpConnectorFactory(private val connectionFactory: ConnectionFactory,
                           private val messageConverter: MessageConverter) : AbstractConnectorFactory() {

    override fun createProducer(): ProducerConnector {
        return AmqpProducerConnector(connectionFactory, messageConverter)
    }

    override fun createConsumer(target: String): ConsumerConnector {
        return AmqpConsumerConnector(target, connectionFactory)
    }
}