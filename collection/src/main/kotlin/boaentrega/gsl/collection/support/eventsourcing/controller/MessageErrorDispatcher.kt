package boaentrega.gsl.collection.support.eventsourcing.controller

import boaentrega.gsl.collection.support.eventsourcing.controller.annotations.ErrorHandler
import boaentrega.gsl.collection.support.eventsourcing.controller.constants.ErrorType
import org.springframework.util.ReflectionUtils
import java.lang.reflect.Method

class MessageErrorDispatcher(private val instance: Any) {
    private val methodHandler: Method?

    init {
        methodHandler = findErrorHandler()
    }

    private fun findErrorHandler(): Method? {
        val methods = ReflectionUtils.getAllDeclaredMethods(instance::class.java)
        return methods.firstOrNull { it.isAnnotationPresent(ErrorHandler::class.java) }
    }

    fun dispatchError(errorType: ErrorType, handleId: String? = null, jsonMessage: Any? = null): Boolean {
        if (methodHandler == null) {
            return true
        }

        val varargs = mutableListOf<Any>()
        methodHandler.parameters.forEach {
            when (it.type) {
                ErrorType::class.java -> varargs.add(errorType)
                String::class.java -> handleId?.let { value -> varargs.add(value) }
                Any::class.java -> jsonMessage?.let { value -> varargs.add(value) }
            }
        }

        return when (methodHandler.invoke(instance, *varargs.toTypedArray())) {
            true -> true
            false -> false
            null -> true
            else -> true
        }
    }
}