package com.example.demo.support.eventsourcing.messages

class CommandMessage : Message {
    companion object{
        val type = MessageType.COMMAND
    }
    constructor(data: Any) : super(CommandMessage.type, data)
    constructor(identifier: String, content: String) : super(CommandMessage.type, identifier, content)
}