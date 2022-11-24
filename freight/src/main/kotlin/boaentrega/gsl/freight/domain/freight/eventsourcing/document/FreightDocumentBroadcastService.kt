package boaentrega.gsl.freight.domain.freight.eventsourcing.document

import boaentrega.gsl.freight.configuration.constants.EventSourcingBeanQualifiers
import boaentrega.gsl.freight.domain.freight.Freight
import boaentrega.gsl.freight.support.eventsourcing.connectors.DedicatedProducerConnector
import boaentrega.gsl.freight.support.eventsourcing.services.AbstractDocumentBroadcastService
import boaentrega.gsl.freight.support.extensions.ClassExtensions.toJsonString
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