package boaentrega.gsl.support.eventsourcing.connectors

interface ConnectorFactory {
    fun createProducer(queue: String): ProducerConnector
    fun createConsumer(queue: String): ConsumerConnector
}