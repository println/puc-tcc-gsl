package boaentrega.gsl.order.domain.order.eventsourcing.document

import boaentrega.gsl.order.configuration.constants.EventSourcingBeanQualifiers
import boaentrega.gsl.order.domain.freight.Freight
import boaentrega.gsl.order.support.eventsourcing.connectors.DedicatedProducerConnector
import boaentrega.gsl.order.support.eventsourcing.services.AbstractDocumentBroadcastService
import boaentrega.gsl.order.support.extensions.ClassExtensions.toJsonString
import org.springframework.beans.factory.annotation.Qualifier
import org.springframework.stereotype.Service

@Service
class FreightDocumentBroadcastService(
        @Qualifier(EventSourcingBeanQualifiers.FREIGHT_DOCUMENT_PRODUCER)
        dedicatedProducerConnector: DedicatedProducerConnector
) : AbstractDocumentBroadcastService<Freight>(dedicatedProducerConnector) {

    override fun release(entity: Freight) {
        val documentJson = entity.toJsonString()
        broadcastRelease(entity.id!!, entity.id!!, documentJson)
    }
}