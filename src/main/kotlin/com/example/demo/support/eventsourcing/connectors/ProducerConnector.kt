package com.example.demo.support.eventsourcing.connectors

import com.example.demo.support.eventsourcing.messages.Message

interface ProducerConnector {
    fun publish(message: Message)
}