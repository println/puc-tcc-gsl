package com.example.demo.domain.controller

import com.example.demo.configuration.constants.EventSourcingBeansConstants
import com.example.demo.domain.messages.ProMessage
import com.example.demo.domain.messages.SuperMessage
import com.example.demo.domain.messages.XptoMessage
import com.example.demo.support.eventsourcing.connectors.ConsumerConnector
import com.example.demo.support.eventsourcing.controller.AbstractConsumerController
import com.example.demo.support.eventsourcing.controller.annotations.ConsumptionHandler
import org.springframework.beans.factory.annotation.Qualifier
import org.springframework.stereotype.Service

//@Service
class UnitConsumerController(
        @Qualifier(EventSourcingBeansConstants.CONTEXT_EVENT_CONSUMER)
        private val consumerConnector: ConsumerConnector
) : AbstractConsumerController(consumerConnector) {

    @ConsumptionHandler(XptoMessage::class)
    fun consumeXptoMessage(message: XptoMessage) {
        //
        println("consumeXptoMessage: ${message.message}")
    }

    @ConsumptionHandler(ProMessage::class)
    fun consumeProMessage(message: ProMessage) {
        println("consumeProMessage: ${message.message}")
    }

    @ConsumptionHandler(SuperMessage::class)
    fun consumeSuperMessage(message: SuperMessage) {
        println("consumeSuperMessage: ${message.message}")
    }
}