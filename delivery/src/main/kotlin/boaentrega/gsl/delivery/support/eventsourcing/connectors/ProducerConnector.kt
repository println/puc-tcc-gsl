package boaentrega.gsl.delivery.support.eventsourcing.connectors

interface ProducerConnector {
    fun publish(messageJson: String, target: String)
}