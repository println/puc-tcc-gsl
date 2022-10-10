package boaentrega.gsl.support.eventsourcing.controller

import boaentrega.gsl.support.eventsourcing.connectors.ConsumerConnector
import boaentrega.gsl.support.extensions.ClassExtensions.logger

class ConsumerBinder(
        private val controller: boaentrega.gsl.support.eventsourcing.controller.AbstractConsumerController,
        private val consumerConnector: ConsumerConnector,
        private val dispatcher: boaentrega.gsl.support.eventsourcing.controller.MessageDispatcher = boaentrega.gsl.support.eventsourcing.controller.MessageDispatcher(controller)
) {
    private val logger = logger()

    init {
        if (!dispatcher.canRun()) {
            logger.error("Can't bind dispatcher to consumer: ${consumerConnector.getId()}")
        } else consumerConnector.setDispatcher { message -> dispatcher.dispatch(message) }
    }

    fun bind() = consumerConnector.start()

    fun unbind() = consumerConnector.stop()
}