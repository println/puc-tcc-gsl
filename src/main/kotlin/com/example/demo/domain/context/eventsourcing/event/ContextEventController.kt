package com.example.demo.domain.context.eventsourcing.event

import com.example.demo.configuration.constants.EventSourcingBeansConstants
import com.example.demo.domain.context.ContextService
import com.example.demo.support.eventsourcing.connectors.ConsumerConnector
import com.example.demo.support.eventsourcing.controller.AbstractConsumerController
import com.example.demo.support.eventsourcing.controller.annotations.ConsumptionHandler
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