package boaentrega.gsl.support.eventsourcing.controller

import boaentrega.gsl.support.eventsourcing.connectors.ConsumerConnector
import boaentrega.gsl.support.extensions.ClassExtensions.logger
import org.springframework.beans.factory.DisposableBean
import org.springframework.boot.context.event.ApplicationReadyEvent
import org.springframework.context.event.EventListener

abstract class AbstractConsumerController(
        private val consumerConnector: ConsumerConnector,
) : DisposableBean {

    private val logger = logger()

    private val binder: ConsumerBinder = ConsumerBinder(this, consumerConnector)

    @EventListener(ApplicationReadyEvent::class)
    fun startConsuming() {
        logger.info("Starting consumer: ${consumerConnector.getId()}")
        binder.bind()
    }

    override fun destroy() {
        logger.info("Closing consumer: ${consumerConnector.getId()}")
        binder.unbind()
    }
}

