package com.example.demo.support.eventsourcing.connectors.dummy

import com.example.demo.support.eventsourcing.connectors.ProducerConnector
import com.example.demo.support.eventsourcing.messages.Message

class GenericProducerConnector(val bean: String) : ProducerConnector {
    override fun publish(message: Message) {
        TODO("Not yet implemented")
    }
}