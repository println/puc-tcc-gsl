package boaentrega.gsl.order.support.eventsourcing.messages

import java.util.*

class DocumentMessage : Message {
    companion object {
        val type = MessageType.DOCUMENT
    }

    constructor(trackId: UUID?, data: Any) : super(trackId, EventMessage.type, data)
    constructor(trackId: UUID?, identifier: String, content: String) : super(trackId, EventMessage.type, identifier, content)
}