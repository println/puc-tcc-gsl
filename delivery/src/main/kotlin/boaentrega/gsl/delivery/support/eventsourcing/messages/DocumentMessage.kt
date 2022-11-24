package boaentrega.gsl.delivery.support.eventsourcing.messages

import java.util.*

class DocumentMessage : Message {
    companion object {
        val type = MessageType.DOCUMENT
    }

    constructor(trackId: UUID?, data: Any) : super(trackId, DocumentMessage.type, data)
    constructor(trackId: UUID?, identifier: String, content: String) : super(trackId, DocumentMessage.type, identifier, content)
}