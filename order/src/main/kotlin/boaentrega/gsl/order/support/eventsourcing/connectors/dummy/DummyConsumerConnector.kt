package boaentrega.gsl.order.support.eventsourcing.connectors.dummy

import boaentrega.gsl.order.support.eventsourcing.connectors.AbstractConsumerConnector
import boaentrega.gsl.order.support.eventsourcing.scope.EventSourcingContext
import boaentrega.gsl.order.support.extensions.ClassExtensions.logger


class DummyConsumerConnector(
        eventSourcingContext: EventSourcingContext,
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