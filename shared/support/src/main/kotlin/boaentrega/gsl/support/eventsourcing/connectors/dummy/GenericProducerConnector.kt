package boaentrega.gsl.support.eventsourcing.connectors.dummy

import boaentrega.gsl.support.eventsourcing.connectors.ProducerConnector
import boaentrega.gsl.support.eventsourcing.messages.Message

class GenericProducerConnector(val bean: String) : ProducerConnector {
    override fun publish(message: Message) {
        TODO("Not yet implemented")
    }
}