package boaentrega.gsl.support.eventsourcing.connectors.dummy

import boaentrega.gsl.support.eventsourcing.connectors.AbstractProducerConnector
import boaentrega.gsl.support.eventsourcing.messages.Message
import boaentrega.gsl.support.eventsourcing.messages.MessageType
import boaentrega.gsl.support.extensions.ClassExtensions.logger
import boaentrega.gsl.support.extensions.ClassExtensions.toJsonString
import boaentrega.gsl.support.extensions.ClassExtensions.toObject
import boaentrega.gsl.support.functions.Functions
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlin.reflect.KClass


class DummyProducerConnector() : AbstractProducerConnector() {

    companion object {
        private object Defaults {
            const val CONSUMPTION = false
        }

        var enableConsumption = Defaults.CONSUMPTION
        val consumers: MutableList<DummyConsumerConnector> = mutableListOf()
        val registry: MutableMap<MessageType, MutableList<Message>> = mutableMapOf()

        fun resetDefaults() {
            enableConsumption = Defaults.CONSUMPTION
            registry.clear()
        }

        inline fun <reified T : Any> getMessageContent(clazz: KClass<T>, index: Int = 1): T? {
            return getMessage(clazz, index)?.let { it.getContentObject() }
        }

        fun getMessage(clazz: KClass<*>, index: Int = 1): Message? {
            val identifier = Functions.Message.extractIdentifier(clazz)
            return registry.values.flatten()
                    .filter { it.identifier == identifier }
                    .getOrNull(index - 1)
        }

        fun getMessage(clazz: KClass<*>, messageType: MessageType = MessageType.EVENT): Message? {
            return getMessages(Pair(clazz, messageType))
                    .firstOrNull()
        }

        private fun getMessages(vararg pairs: Pair<KClass<*>, MessageType>): List<Message> {
            val identifies = pairs.map { Pair(Functions.Message.extractIdentifier(it.first), it.second) }
            return identifies
                    .mapNotNull { registry[it.second]?.first { item -> item.identifier == it.first } }
        }
    }

    private val logger = logger()

    init {
        logger.info("Creating Dummy Producer Connector")
    }

    override fun send(messageJson: String, target: String) {
        val message = messageJson.toObject<Message>()

        if (registry.containsKey(message.type)) {
            registry[message.type]?.add(message)
        } else {
            registry[message.type] = mutableListOf(message)
        }

        if (enableConsumption) {
            GlobalScope.launch(Dispatchers.Default) {
                var consumer = consumers.first { consumer -> consumer.target == target }
                if(consumer != null) {
                    logger.info("Dispatching ${message.type}: ${message.toJsonString()}")
                    consumer.consume(message)
                } else {
                    logger.error("LOST [${message.type}]: ${message.toJsonString()}")
                }
            }
        }
    }


}