package boaentrega.gsl.support.eventsourcing.connectors.dummy

import boaentrega.gsl.support.eventsourcing.connectors.AbstractConsumerConnector

class DummyConsumerConnector(private val id: String) : AbstractConsumerConnector() {
    override fun startConsumer() {
    }

    override fun stopConsumer() {
    }

    override fun getId(): String {
        return "${this::class.simpleName.toString()}: $id"
    }

}