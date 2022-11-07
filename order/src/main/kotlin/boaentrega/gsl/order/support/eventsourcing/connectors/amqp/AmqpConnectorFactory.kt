package boaentrega.gsl.order.support.eventsourcing.connectors.amqp

import boaentrega.gsl.order.support.eventsourcing.connectors.AbstractConnectorFactory
import boaentrega.gsl.order.support.eventsourcing.connectors.ConsumerConnector
import boaentrega.gsl.order.support.eventsourcing.connectors.ProducerConnector
import boaentrega.gsl.order.support.eventsourcing.scope.EventSourcingContext
import org.springframework.amqp.rabbit.connection.ConnectionFactory
import org.springframework.amqp.support.converter.MessageConverter

class AmqpConnectorFactory(private val connectionFactory: ConnectionFactory,
                           private val messageConverter: MessageConverter) : AbstractConnectorFactory() {

    override fun createProducer(): ProducerConnector {
        return AmqpProducerConnector(connectionFactory, messageConverter)
    }

    override fun createConsumer(eventSourcingContext: EventSourcingContext, target: String): ConsumerConnector {
        return AmqpConsumerConnector(eventSourcingContext, target, connectionFactory)
    }
}