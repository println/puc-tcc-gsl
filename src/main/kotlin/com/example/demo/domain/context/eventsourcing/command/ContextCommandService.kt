package com.example.demo.domain.context.eventsourcing.command


import com.example.demo.configuration.constants.EventSourcingBeansConstants
import com.example.demo.support.eventsourcing.connectors.ProducerConnector
import com.example.demo.support.eventsourcing.messages.CommandMessage
import com.example.demo.support.extensions.ClassExtensions.logger
import com.example.demo.support.extensions.ClassExtensions.toJsonString
import org.springframework.beans.factory.annotation.Qualifier
import org.springframework.stereotype.Service

@Service
class ContextCommandService(
        @Qualifier(EventSourcingBeansConstants.CONTEXT_COMMAND_PRODUCER)
        private val producerConnector: ProducerConnector) {

    private val logger = logger()

    fun send(data: Any) {
        val message = CommandMessage(data)
        producerConnector.publish(message)
        logger.info("Command has been sent: ${message.toJsonString()}")
    }

}