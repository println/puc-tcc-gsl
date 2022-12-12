package boaentrega.gsl.services.monolith.domain.order.eventsourcing.document

import boaentrega.gsl.configuration.constants.EventSourcingBeanQualifiers
import boaentrega.gsl.services.monolith.domain.order.Order
import boaentrega.gsl.support.eventsourcing.connectors.DedicatedProducerConnector
import boaentrega.gsl.support.eventsourcing.services.AbstractDocumentBroadcastService
import boaentrega.gsl.support.extensions.ClassExtensions.toJsonString
import org.springframework.beans.factory.annotation.Qualifier
import org.springframework.stereotype.Service

@Service
class OrderDocumentBroadcastService(
        @Qualifier(EventSourcingBeanQualifiers.ORDER_DOCUMENT_PRODUCER)
        dedicatedProducerConnector: DedicatedProducerConnector
) : AbstractDocumentBroadcastService<Order>(dedicatedProducerConnector) {

    override fun release(entity: Order) {
        val documentJson = entity.toJsonString()
        broadcastRelease(entity.id!!, entity.id!!, documentJson)
    }
}