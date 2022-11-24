package boaentrega.gsl.collection.support.eventsourcing.connectors.dummy

import boaentrega.gsl.collection.support.eventsourcing.connectors.AbstractConsumerConnector
import boaentrega.gsl.collection.support.extensions.ClassExtensions.logger


class DummyConsumerConnector(
        val target: String) : AbstractConsumerConnector() {

    private val logger = logger()

    init {
        logger.info("Creating Dummy Consumer Connector: $target")
        DummyProducerConnector.consumers.add(this)
    }

    override fun startConsumer() {
    }

    override fun stopConsumer() {
    }

    override fun getId(): String {
        return target
    }

}