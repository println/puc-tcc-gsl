package boaentrega.gsl.order.domain.context.eventsourcing.event

import boaentrega.gsl.order.configuration.constants.EventSourcingBeansConstants
import boaentrega.gsl.order.domain.context.ContextService
import boaentrega.gsl.support.eventsourcing.connectors.ConsumerConnector
import boaentrega.gsl.support.eventsourcing.controller.AbstractConsumerController
import boaentrega.gsl.support.eventsourcing.controller.annotations.ConsumptionHandler
import gsl.schemas.FreightPurchasedEvent
import org.springframework.beans.factory.annotation.Qualifier
import org.springframework.stereotype.Component

@Component
class ContextEventController(
        @Qualifier(EventSourcingBeansConstants.CONTEXT_EVENT_CONSUMER)
        private val consumerConnector: ConsumerConnector,
        private val service: ContextService
) : AbstractConsumerController(consumerConnector) {

    @ConsumptionHandler(FreightPurchasedEvent::class)
    fun listen(event: FreightPurchasedEvent) {
        println(event)
    }
}