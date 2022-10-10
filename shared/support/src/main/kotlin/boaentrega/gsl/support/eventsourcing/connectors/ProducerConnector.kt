package boaentrega.gsl.support.eventsourcing.connectors

import boaentrega.gsl.support.eventsourcing.messages.Message

interface ProducerConnector {
    fun publish(message: Message)
}