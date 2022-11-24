package boaentrega.gsl.freight.support.eventsourcing.controller

import boaentrega.gsl.freight.support.eventsourcing.connectors.ConsumerConnector
import boaentrega.gsl.freight.support.extensions.ClassExtensions.logger

class ConsumerBinder(
        private val controller: AbstractConsumerController,
        private val consumerConnector: ConsumerConnector,
        private val dispatcher: MessageDispatcher = MessageDispatcher(controller, consumerConnector.getId())
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