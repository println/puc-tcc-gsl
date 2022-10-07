package com.example.demo.support.eventsourcing.messages

import com.example.demo.support.extensions.ClassExtensions.toJsonString
import com.example.demo.support.functions.Functions
import com.fasterxml.jackson.annotation.JsonCreator
import org.apache.avro.specific.SpecificRecordBase

open class Message {
    val type: MessageType
    val identifier: String
    val content: String

    @JsonCreator
    constructor(type: MessageType, identifier: String, content: String) {
        this.type = type
        this.identifier = identifier
        this.content = content
    }

    constructor(type: MessageType, data: Any) {
        this.type = type
        this.identifier = Functions.Message.extractIdentifier(data)

        this.content = if (data is SpecificRecordBase) (data as SpecificRecordBase).toString() else data.toJsonString()
    }
}
