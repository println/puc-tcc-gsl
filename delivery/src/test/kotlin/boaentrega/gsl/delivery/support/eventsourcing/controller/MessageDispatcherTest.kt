package boaentrega.gsl.delivery.support.eventsourcing.controller

import boaentrega.gsl.delivery.support.eventsourcing.controller.annotations.ConsumptionHandler
import boaentrega.gsl.delivery.support.eventsourcing.controller.annotations.ErrorHandler
import boaentrega.gsl.delivery.support.eventsourcing.controller.constants.ErrorType
import boaentrega.gsl.delivery.support.eventsourcing.messages.CommandMessage
import boaentrega.gsl.delivery.support.eventsourcing.messages.EventMessage
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertNotNull
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import java.util.*

internal class MessageDispatcherTest {

    private companion object {
        var message: Any? = null
        var handleId: String? = null
        var errorType: ErrorType? = null
        var id = UUID.randomUUID()

        fun clear() {
            message = null
            handleId = null
            errorType = null
        }
    }

    private lateinit var dispatcher: MessageDispatcher

    @BeforeEach
    fun setup() {
        id = UUID.randomUUID()
        dispatcher = MessageDispatcher(Controllers.SimpleController(), "test")
    }

    @AfterEach
    fun clearMessage() {
        clear()
    }

    @Test
    fun dispatchByHandleId() {
        dispatcher.dispatch(EventMessage(id, Constants.HANDLE_ID_ANY, Constants.EMPTY_MESSAGE))
        assertNotNull(message)
    }

    @Test
    fun dispatchErrorNotFound() {
        dispatcher.dispatch(EventMessage(id, Constants.HANDLE_ID_ERROR, Constants.EMPTY_MESSAGE))
        assertNotNull(errorType)
        assertNotNull(message)
        assertNotNull(handleId)
        assertEquals(ErrorType.NOT_FOUND, errorType)
        assertEquals(Constants.EMPTY_MESSAGE, message)
        assertEquals(Constants.HANDLE_ID_ERROR, handleId)
    }

    @Test
    fun dispatchErrorEmptyHandlers() {
        dispatcher = MessageDispatcher(Controllers.WithoutController(), "test")
        dispatcher.dispatch(EventMessage(id, Constants.HANDLE_ID_ANY, Constants.EMPTY_MESSAGE))
        assertNotNull(errorType)
        assertNotNull(handleId)
        assertNotNull(message)
        assertEquals(ErrorType.EMPTY_HANDLERS, errorType)
        assertEquals(Constants.HANDLE_ID_ANY, handleId)
        assertEquals(Constants.EMPTY_MESSAGE, message)
    }

    @Test
    fun dispatchErrorParseFailed() {
        dispatcher.dispatch(EventMessage(id, Constants.HANDLE_ID_ANY, Constants.WRONG_MESSAGE))
        assertNotNull(errorType)
        assertNotNull(handleId)
        assertNotNull(message)
        assertEquals(ErrorType.PARSE_FAILED, errorType)
        assertEquals(Constants.HANDLE_ID_ANY, handleId)
        assertEquals(Constants.WRONG_MESSAGE, message)
    }

    private object Controllers {
        class WithoutController : ErrorController()

        class SimpleController : ErrorController() {
            @ConsumptionHandler(Any::class)
            fun consume(obj: Any) {
                message = obj
            }

            @ConsumptionHandler(CommandMessage::class)
            fun consume(obj: CommandMessage) {
                message = obj
            }
        }

        open class ErrorController {
            @ErrorHandler
            open fun consumeError(errorType: ErrorType, handleId: String, message: Any) {
                Companion.errorType = errorType
                Companion.handleId = handleId
                Companion.message = message
            }
        }
    }
}