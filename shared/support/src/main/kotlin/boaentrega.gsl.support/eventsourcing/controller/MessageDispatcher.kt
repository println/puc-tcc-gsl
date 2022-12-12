package boaentrega.gsl.support.eventsourcing.controller

import boaentrega.gsl.support.eventsourcing.controller.annotations.ConsumptionHandler
import boaentrega.gsl.support.eventsourcing.controller.constants.ErrorType
import boaentrega.gsl.support.eventsourcing.messages.Message
import boaentrega.gsl.support.extensions.ClassExtensions.logger
import boaentrega.gsl.support.extensions.ClassExtensions.toObject
import boaentrega.gsl.support.functions.Functions
import org.springframework.util.ReflectionUtils
import java.lang.reflect.Method

class MessageDispatcher(
        private val instance: Any,
        private val sourceId: String
) {
    companion object {
        private var dispatchTable: MutableMap<String, Triple<Class<*>, Method, Any>> = mutableMapOf()
        fun clear() = dispatchTable.clear()
    }


    private val logger = logger()
    private val errorHandler = MessageErrorDispatcher(instance)

    init {
        dispatchTable.putAll(extractDispatchTable())
        if (!canRun()) {
            logger.error("[$sourceId] Dispatcher not has handle methods")
        }
    }

    private fun extractDispatchTable(): Map<String, Triple<Class<*>, Method, Any>> {
        val methods = ReflectionUtils.getAllDeclaredMethods(instance::class.java)
        return methods.filter { it.isAnnotationPresent(ConsumptionHandler::class.java) }
                .filter { it.parameterCount == 1 }
                .associateBy(
                        { Functions.Message.extractIdentifier(it.getAnnotation(ConsumptionHandler::class.java)) },
                        { Triple(it.parameterTypes[0], it, instance) })
    }

    fun dispatch(message: Message): Boolean {
        if (dispatchTable.isEmpty()) {
            logger.error("[$sourceId] Not have handlers")
            errorHandler.dispatchError(ErrorType.EMPTY_HANDLERS, message.identifier, message.content)
            return false
        }

        if (!dispatchTable.containsKey(message.identifier)) {
            logger.error("[$sourceId] Handle Id not found")
            return errorHandler.dispatchError(ErrorType.NOT_FOUND, message.identifier, message.content)
        }

        if (!Functions.Json.isValid(message.content)) {
            logger.error("[$sourceId] Can't parse message content")
            return errorHandler.dispatchError(ErrorType.PARSE_FAILED, message.identifier, message.content)
        }

        val (type, handler, instance) = dispatchTable.getValue(message.identifier)
        val typedMessage = message.content.toObject(type)

        return when (handler.invoke(instance, typedMessage)) {
            true -> true
            false -> false
            null -> true
            else -> true
        }
    }

    fun canRun() = dispatchTable.isNotEmpty()

}