package boaentrega.gsl.freight.support.eventsourcing.messages

import java.util.*

class EventMessage : Message {
    companion object {
        val type = MessageType.EVENT
    }

    constructor(trackId: UUID?, data: Any) : super(trackId, EventMessage.type, data)
    constructor(trackId: UUID?, identifier: String, content: String) : super(trackId, EventMessage.type, identifier, content)
}