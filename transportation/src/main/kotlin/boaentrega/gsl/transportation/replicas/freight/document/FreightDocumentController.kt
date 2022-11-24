package boaentrega.gsl.transportation.replicas.freight.document

import boaentrega.gsl.transportation.configuration.constants.EventSourcingBeanQualifiers
import boaentrega.gsl.transportation.replicas.freight.FreightDoc
import boaentrega.gsl.transportation.replicas.freight.FreightDocService
import boaentrega.gsl.transportation.support.eventsourcing.connectors.ConsumerConnector
import boaentrega.gsl.transportation.support.eventsourcing.controller.AbstractConsumerController
import boaentrega.gsl.transportation.support.eventsourcing.controller.annotations.ConsumptionHandler
import boaentrega.gsl.transportation.support.extensions.ClassExtensions.toObject
import gsl.schemas.DocumentExpired
import gsl.schemas.DocumentReleased
import org.springframework.beans.factory.annotation.Qualifier
import org.springframework.stereotype.Component

@Component
class FreightDocumentController(
        @Qualifier(EventSourcingBeanQualifiers.FREIGHT_DOCUMENT_CONSUMER)
        private val consumerConnector: ConsumerConnector,
        private val service: FreightDocService
) : AbstractConsumerController(consumerConnector) {

    @ConsumptionHandler(DocumentReleased::class)
    fun saveDocument(document: DocumentReleased) {
        val data = document.document.toObject<FreightDoc>()
        service.save(document.documentId, data)
    }

    @ConsumptionHandler(DocumentExpired::class)
    fun removeDocument(document: DocumentExpired) {
        service.delete(document.documentId)
    }
}