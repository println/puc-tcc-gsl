package com.example.demo.domain.context.eventsourcing.command

import com.example.demo.configuration.constants.EventSourcingBeansConstants
import com.example.demo.domain.context.ContextService
import com.example.demo.support.eventsourcing.connectors.ConsumerConnector
import com.example.demo.support.eventsourcing.controller.AbstractConsumerController
import com.example.demo.support.eventsourcing.controller.annotations.ConsumptionHandler
import com.example.demo.support.extensions.ClassExtensions.toJsonString
import gsl.schemas.FreightPurchaseCommand
import org.springframework.beans.factory.annotation.Qualifier
import org.springframework.stereotype.Component

@Component
class ContextCommandController(
        @Qualifier(EventSourcingBeansConstants.CONTEXT_COMMAND_CONSUMER)
        private val consumerConnector: ConsumerConnector,
        private val service: ContextService
) : AbstractConsumerController(consumerConnector) {

    @ConsumptionHandler(FreightPurchaseCommand::class)
    fun receive(command: FreightPurchaseCommand) {
        println(command)
        service.applyCommand(command)
    }
}