package boaentrega.gsl.order.support.eventsourcing.connectors.dummy

import boaentrega.gsl.order.support.eventsourcing.connectors.AbstractProducerConnector
import boaentrega.gsl.order.support.extensions.ClassExtensions.logger


class DummyProducerConnector() : AbstractProducerConnector() {

    private val logger = logger()

    init{
        logger.info("Creating Dummy Producer Connector")
    }

    override fun send(messageJson: String, target: String) {

    }
}