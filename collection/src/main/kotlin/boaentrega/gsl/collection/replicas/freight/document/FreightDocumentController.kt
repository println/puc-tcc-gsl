package boaentrega.gsl.collection.replicas.freight.document

import boaentrega.gsl.collection.configuration.constants.EventSourcingBeanQualifiers
import boaentrega.gsl.collection.replicas.freight.FreightDoc
import boaentrega.gsl.collection.replicas.freight.FreightDocService
import boaentrega.gsl.collection.support.eventsourcing.connectors.ConsumerConnector
import boaentrega.gsl.collection.support.eventsourcing.controller.AbstractConsumerController
import boaentrega.gsl.collection.support.eventsourcing.controller.annotations.ConsumptionHandler
import boaentrega.gsl.collection.support.extensions.ClassExtensions.toObject
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