package boaentrega.gsl.order.domain.controller

import boaentrega.gsl.order.configuration.constants.EventSourcingBeansConstants
import boaentrega.gsl.order.domain.messages.ProMessage
import boaentrega.gsl.order.domain.messages.SuperMessage
import boaentrega.gsl.order.domain.messages.XptoMessage
import boaentrega.gsl.support.eventsourcing.connectors.ConsumerConnector
import boaentrega.gsl.support.eventsourcing.controller.AbstractConsumerController
import boaentrega.gsl.support.eventsourcing.controller.annotations.ConsumptionHandler
import org.springframework.beans.factory.annotation.Qualifier
import org.springframework.stereotype.Service

//@Service
class UnitConsumerController(
        @Qualifier(EventSourcingBeansConstants.CONTEXT_EVENT_CONSUMER)
        private val consumerConnector: ConsumerConnector
) : AbstractConsumerController(consumerConnector) {

    @ConsumptionHandler(XptoMessage::class)
    fun consumeXptoMessage(message: XptoMessage) {
        //
        println("consumeXptoMessage: ${message.message}")
    }

    @ConsumptionHandler(ProMessage::class)
    fun consumeProMessage(message: ProMessage) {
        println("consumeProMessage: ${message.message}")
    }

    @ConsumptionHandler(SuperMessage::class)
    fun consumeSuperMessage(message: SuperMessage) {
        println("consumeSuperMessage: ${message.message}")
    }
}