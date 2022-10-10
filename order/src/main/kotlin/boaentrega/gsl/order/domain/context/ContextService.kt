package boaentrega.gsl.order.domain.context

import boaentrega.gsl.order.domain.context.eventsourcing.command.ContextCommandService
import boaentrega.gsl.order.domain.context.eventsourcing.event.ContextEventService
import gsl.schemas.FreightPurchaseCommand
import gsl.schemas.FreightPurchasedEvent
import org.springframework.stereotype.Service

@Service
class ContextService(
        private val commandService: ContextCommandService,
        private val eventService: ContextEventService) {

    fun startFire(message: String) {
        commandService.send(FreightPurchaseCommand("client", "consumer", message))
    }

    fun applyCommand(command: FreightPurchaseCommand) {
        eventService.emit(FreightPurchasedEvent(command.client, command.consumer, command.product))
    }
}