package boaentrega.gsl.order.support.eventsourcing.connectors

interface ProducerConnector {
    fun publish(messageJson: String, target: String)
}