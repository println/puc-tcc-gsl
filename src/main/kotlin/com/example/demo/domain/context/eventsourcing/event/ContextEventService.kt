package com.example.demo.domain.context.eventsourcing.event

import com.example.demo.configuration.constants.EventSourcingBeansConstants
import com.example.demo.support.eventsourcing.connectors.ProducerConnector
import com.example.demo.support.eventsourcing.messages.EventMessage
import com.example.demo.support.extensions.ClassExtensions.logger
import com.example.demo.support.extensions.ClassExtensions.toJsonString
import org.springframework.beans.factory.annotation.Qualifier
import org.springframework.stereotype.Service

@Service
class ContextEventService(
        @Qualifier(EventSourcingBeansConstants.CONTEXT_EVENT_PRODUCER)
        private val producerConnector: ProducerConnector) {

    private val logger = logger()

    fun emit(data: Any) {
        val message = EventMessage(data)
        producerConnector.publish(message)
        logger.info("Event has been emitted: ${message.toJsonString()}")
    }
}