package boaentrega.gsl.services.freight.domain.freight.eventsourcing.document

import boaentrega.gsl.configuration.constants.EventSourcingBeanQualifiers
import boaentrega.gsl.services.freight.configuration.constants.Qualifiers
import boaentrega.gsl.services.freight.domain.freight.Freight
import boaentrega.gsl.support.eventsourcing.connectors.DedicatedProducerConnector
import boaentrega.gsl.support.eventsourcing.services.AbstractDocumentBroadcastService
import boaentrega.gsl.support.extensions.ClassExtensions.toJsonString
import org.springframework.beans.factory.annotation.Qualifier
import org.springframework.stereotype.Service

@Service
@Qualifier(Qualifiers.APP_DOCUMENT_SERVICE)
class FreightDocumentBroadcastService(
        @Qualifier(EventSourcingBeanQualifiers.FREIGHT_DOCUMENT_PRODUCER)
        dedicatedProducerConnector: DedicatedProducerConnector
) : AbstractDocumentBroadcastService<Freight>(dedicatedProducerConnector) {

    override fun release(entity: Freight) {
        val documentJson = entity.toJsonString()
        broadcastRelease(entity.id!!, entity.id!!, documentJson)
    }
}