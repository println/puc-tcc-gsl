package boaentrega.gsl.collection.support.eventsourcing.connectors

interface ProducerConnector {
    fun publish(messageJson: String, target: String)
}