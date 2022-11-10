package boaentrega.gsl.order.support.eventsourcing.connectors.dummy

import boaentrega.gsl.order.support.eventsourcing.connectors.AbstractProducerConnector
import boaentrega.gsl.order.support.eventsourcing.messages.Message
import boaentrega.gsl.order.support.extensions.ClassExtensions.logger
import boaentrega.gsl.order.support.extensions.ClassExtensions.toObject
import boaentrega.gsl.order.support.functions.Functions


class DummyProducerConnector() : AbstractProducerConnector() {

    companion object {
        val consumers: MutableList<DummyConsumerConnector> = mutableListOf()
        val registry: MutableMap<String, MutableList<String>> = mutableMapOf()

        fun findAll(clazz: Class<*>): List<Message> {
            val ref = Functions.Message.extractIdentifier(clazz)
            val bucketRef = ref.split("-").last()
            return registry
                    .filterKeys { it.contains(bucketRef) }
                    .map { it.value }
                    .flatten()
                    .filter { it.contains(ref) }
                    .map { it.toObject() }
        }

        fun clearMessages() = registry.clear()
    }

    private val logger = logger()

    init {
        logger.info("Creating Dummy Producer Connector")
    }

    override fun send(messageJson: String, target: String) {
        if (registry.containsKey(target)) {
            registry[target]?.add(messageJson)
        } else {
            registry[target] = mutableListOf(messageJson)
        }
        val message = messageJson.toObject(Message::class.java)
        consumers.first { consumer -> consumer.target == target }.consume(message)
    }


}