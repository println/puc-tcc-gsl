package boaentrega.gsl.order.support.eventsourcing.connectors.dummy

import boaentrega.gsl.order.support.eventsourcing.connectors.AbstractConsumerConnector

class GenericConsumerConnector(val bean: String) : boaentrega.gsl.order.support.eventsourcing.connectors.AbstractConsumerConnector() {

    override fun afterStart() {
    }

    override fun getId(): String {
        return "dummy"
    }

    override fun startConsumer() {
        println("lauched!")
    }

    override fun stopConsumer() {
        println("shutdown!")
    }
}