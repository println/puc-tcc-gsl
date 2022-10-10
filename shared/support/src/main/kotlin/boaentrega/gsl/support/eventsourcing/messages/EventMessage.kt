package boaentrega.gsl.support.eventsourcing.messages

class EventMessage : Message {
    companion object {
        val type = MessageType.EVENT
    }

    constructor(data: Any) : super(EventMessage.type, data)
    constructor(identifier: String, content: String) : super(EventMessage.type, identifier, content)
}