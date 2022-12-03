package boaentrega.gsl.support.eventsourcing.connectors

interface ProducerConnector {
    fun publish(messageJson: String, target: String)
}