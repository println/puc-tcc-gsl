package boaentrega.gsl.collection.support.eventsourcing.controller

import boaentrega.gsl.collection.support.eventsourcing.controller.annotations.ErrorHandler
import boaentrega.gsl.collection.support.eventsourcing.controller.constants.ErrorType
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test

internal class MessageErrorDispatcherTest {

    private companion object {
        var message: Any? = null
        var handleId: String? = null
        var errorType: ErrorType? = null

        fun clear() {
            message = null
            handleId = null
            errorType = null
        }
    }

    @AfterEach
    fun clearMessage() {
        clear()
    }

    @Test
    fun dispatchErrorWithErrorTypeOnly() {
        val dispatcher = MessageErrorDispatcher(Controllers.SimpleErrorController())
        dispatcher.dispatchError(ErrorType.NOT_FOUND, Constants.HANDLE_ID_ERROR)
        assertNotNull(errorType)
        assertEquals(ErrorType.NOT_FOUND, errorType)
        assertNull(handleId)
        assertEquals(null, handleId)
        assertNull(message)
        assertEquals(null, message)
    }

    @Test
    fun dispatchErrorWithErrorTypeAndMessage() {
        val dispatcher = MessageErrorDispatcher(Controllers.ErrorWithHandleIdController())
        dispatcher.dispatchError(ErrorType.NOT_FOUND, Constants.HANDLE_ID_ERROR)
        assertNotNull(errorType)
        assertEquals(ErrorType.NOT_FOUND, errorType)
        assertNotNull(handleId)
        assertEquals(Constants.HANDLE_ID_ERROR, handleId)
        assertNull(message)
        assertEquals(null, message)
    }

    @Test
    fun dispatchFullError() {
        val dispatcher = MessageErrorDispatcher(Controllers.ErrorFullController())
        dispatcher.dispatchError(ErrorType.NOT_FOUND, Constants.HANDLE_ID_ERROR, Constants.EMPTY_MESSAGE)
        assertNotNull(errorType)
        assertEquals(ErrorType.NOT_FOUND, errorType)
        assertNotNull(handleId)
        assertEquals(Constants.HANDLE_ID_ERROR, handleId)
        assertNotNull(message)
        assertEquals(Constants.EMPTY_MESSAGE, message)
    }

    @Test
    fun dispatchReturnTrue() {
        val dispatcher = MessageErrorDispatcher(Controllers.ErrorReturnTrueController())
        val result = dispatcher.dispatchError(ErrorType.NOT_FOUND, Constants.HANDLE_ID_ERROR, Constants.EMPTY_MESSAGE)
        assertTrue(result)
    }

    @Test
    fun dispatchReturnFalse() {
        val dispatcher = MessageErrorDispatcher(Controllers.ErrorReturnFalseController())
        val result = dispatcher.dispatchError(ErrorType.NOT_FOUND, Constants.HANDLE_ID_ERROR, Constants.EMPTY_MESSAGE)
        assertFalse(result)
    }

    @Test
    fun dispatchReturnNull() {
        val dispatcher = MessageErrorDispatcher(Controllers.ErrorReturnNullController())
        val result = dispatcher.dispatchError(ErrorType.NOT_FOUND, Constants.HANDLE_ID_ERROR, Constants.EMPTY_MESSAGE)
        assertTrue(result)
    }


    @Test
    fun dispatchNoReturn() {
        val dispatcher = MessageErrorDispatcher(Controllers.ErrorNoReturnController())
        val result = dispatcher.dispatchError(ErrorType.NOT_FOUND, Constants.HANDLE_ID_ERROR, Constants.EMPTY_MESSAGE)
        assertTrue(result)
    }

    private object Controllers {
        class SimpleErrorController {
            @ErrorHandler
            fun consumeError(errorType: ErrorType) {
                MessageErrorDispatcherTest.errorType = errorType
            }
        }

        class ErrorWithHandleIdController {
            @ErrorHandler
            fun consumeError(errorType: ErrorType, handleId: String) {
                MessageErrorDispatcherTest.handleId = handleId
                MessageErrorDispatcherTest.errorType = errorType
            }
        }

        class ErrorFullController {
            @ErrorHandler
            fun consumeError(errorType: ErrorType, handleId: String, message: Any) {
                MessageErrorDispatcherTest.errorType = errorType
                MessageErrorDispatcherTest.handleId = handleId
                MessageErrorDispatcherTest.message = message
            }
        }

        class ErrorNoReturnController {
            @ErrorHandler
            fun consumeError(errorType: ErrorType, handleId: String, message: Any) {
            }
        }

        class ErrorReturnTrueController {
            @ErrorHandler
            fun consumeError(errorType: ErrorType, handleId: String, message: Any): Boolean {
                return true
            }
        }

        class ErrorReturnFalseController {
            @ErrorHandler
            fun consumeError(errorType: ErrorType, handleId: String, message: Any): Boolean {
                return false
            }
        }

        class ErrorReturnNullController {
            @ErrorHandler
            fun consumeError(errorType: ErrorType, handleId: String, message: Any): Any? {
                return null
            }
        }
    }
}