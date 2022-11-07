package boaentrega.gsl.order.support.eventsourcing.connectors

import boaentrega.gsl.order.configuration.config.EventSourcingScopeConfig
import boaentrega.gsl.order.support.eventsourcing.messages.Message
import boaentrega.gsl.order.support.eventsourcing.scope.EventSourcingContext
import boaentrega.gsl.order.support.eventsourcing.scope.EventSourcingScope
import boaentrega.gsl.order.support.extensions.ClassExtensions.logger
import org.springframework.context.annotation.AnnotationConfigApplicationContext


abstract class AbstractConsumerConnector() : ConsumerConnector {

    private val logger = logger()
    private var isRunning = false
    private var dispatcher: ((Message) -> Boolean)? = null

    init {
        logger.info("Creating Consumer Connector: ${getId()}")
    }

    override fun setDispatcher(handler: (message: Message) -> Boolean) {
        if (hasDispatcher()) {
            return
        }
        dispatcher = handler
        logger.info("Defined dispatcher to: ${getId()}")
    }

    override fun hasDispatcher(): Boolean = dispatcher != null

    override fun start() {
        if (isRunning) {
            logger.info("Already running: ${getId()}")
            return
        }
        if (!hasDispatcher()) {
            logger.info("No dispatcher to: ${getId()}")
            return
        }
        startConsumer()
        this.isRunning = true
        logger.info("Consumer is started: ${getId()}")
        afterStart()
    }

    override fun consume(message: Message) {
        if (!hasDispatcher()) {
            logger.info("No dispatcher to: ${getId()}")
            return
        }
       // val bean = eventSourcingScope.get()
//        ctx.use { ctx ->
//            ctx.register(EventSourcingScope::class.java)
//            ctx.refresh()
//            ctx.getBean()
//            val dispatched = dispatcher?.invoke(message)
//        }
    }

    override fun stop() {
        if (!isRunning) {
            logger.info("Already stopped: ${getId()}")
            return
        }

        this.isRunning = false
        dispatcher = null
        startConsumer()
        logger.info("Consumer is stopped: ${getId()}")
    }

    override fun destroy() {
        stop()
    }

    internal open fun afterStart() {}

    internal abstract fun startConsumer()
    internal abstract fun stopConsumer()
}