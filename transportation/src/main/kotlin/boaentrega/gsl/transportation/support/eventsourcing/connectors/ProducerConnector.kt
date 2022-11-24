package boaentrega.gsl.transportation.support.eventsourcing.connectors

interface ProducerConnector {
    fun publish(messageJson: String, target: String)
}