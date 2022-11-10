package boaentrega.gsl.order.support.eventsourcing.connectors

import boaentrega.gsl.order.support.extensions.ClassExtensions.logger

abstract class AbstractProducerConnector : ProducerConnector {

    private val logger = logger()

    final override fun publish(messageJson: String, target: String) {
        send(messageJson, target)
        logger.info("Message has been published to [$target]: $messageJson")
    }

    protected abstract fun send(messageJson: String, target: String)
}