package boaentrega.gsl.support.eventsourcing.connectors

import boaentrega.gsl.support.eventsourcing.messages.Message
import boaentrega.gsl.support.extensions.ClassExtensions.logger
import boaentrega.gsl.support.extensions.ClassExtensions.toJsonString

class DedicatedProducerConnector(
        private val producerConnector: ProducerConnector,
        private val target: String) {

    private val logger = logger()

    init {
        logger.info("Creating Dedicated Producer: $target")
    }

    fun publish(message: Message) {
        producerConnector.publish(message.toJsonString(), target)
    }

    fun getId(): String {
        return target
    }
}