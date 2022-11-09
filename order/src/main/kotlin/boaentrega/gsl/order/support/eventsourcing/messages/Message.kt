package boaentrega.gsl.order.support.eventsourcing.messages

import boaentrega.gsl.order.support.extensions.AvroExtensions.toJsonString
import boaentrega.gsl.order.support.extensions.ClassExtensions.toJsonString
import boaentrega.gsl.order.support.functions.Functions
import com.fasterxml.jackson.annotation.JsonCreator
import org.apache.avro.specific.SpecificRecordBase
import java.util.*

open class Message {
    val trackId: UUID?
    val type: MessageType
    val identifier: String
    val content: String

    @JsonCreator
    constructor(trackId: UUID?, type: MessageType, identifier: String, content: String) {
        this.trackId = trackId
        this.type = type
        this.identifier = identifier
        this.content = content
    }

    constructor(trackId: UUID?, type: MessageType, data: Any) {
        this.trackId = trackId
        this.type = type
        this.identifier = Functions.Message.extractIdentifier(data)
        this.content = if (data is SpecificRecordBase) data.toJsonString() else data.toJsonString()
    }

}
