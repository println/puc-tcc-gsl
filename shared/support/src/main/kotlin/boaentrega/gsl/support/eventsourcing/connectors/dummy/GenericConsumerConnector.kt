package boaentrega.gsl.support.eventsourcing.connectors.dummy

import boaentrega.gsl.support.eventsourcing.connectors.AbstractConsumerConnector
import boaentrega.gsl.support.eventsourcing.messages.Message
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.module.kotlin.KotlinModule

class GenericConsumerConnector(val bean: String) : AbstractConsumerConnector() {

    override fun afterStart() {
    }

    override fun getId(): String {
        return "teste"
    }

    override fun startConsumer() {
        println("lauched!")
    }

    override fun stopConsumer() {
        println("shutdown!")
    }
}