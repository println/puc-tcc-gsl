package boaentrega.gsl.order.support.eventsourcing.messages

import java.util.*

class CommandMessage : Message {
    companion object{
        val type = MessageType.COMMAND
    }
    constructor(trackId: UUID?, data: Any) : super(trackId, CommandMessage.type, data)
    constructor(trackId: UUID?, identifier: String, content: String) : super(trackId, CommandMessage.type, identifier, content)
}