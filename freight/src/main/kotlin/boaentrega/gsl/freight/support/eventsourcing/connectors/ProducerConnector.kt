package boaentrega.gsl.freight.support.eventsourcing.connectors

interface ProducerConnector {
    fun publish(messageJson: String, target: String)
}