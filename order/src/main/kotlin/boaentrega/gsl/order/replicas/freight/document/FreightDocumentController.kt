package boaentrega.gsl.order.replicas.freight.document


import boaentrega.gsl.order.configuration.constants.EventSourcingBeanQualifiers
import boaentrega.gsl.order.replicas.freight.FreightDocument
import boaentrega.gsl.order.replicas.freight.FreightDocumentService
import boaentrega.gsl.order.support.eventsourcing.connectors.ConsumerConnector
import boaentrega.gsl.order.support.eventsourcing.controller.AbstractConsumerController
import boaentrega.gsl.order.support.eventsourcing.controller.annotations.ConsumptionHandler
import boaentrega.gsl.order.support.extensions.ClassExtensions.toObject
import gsl.schemas.DocumentRemove
import gsl.schemas.DocumentSave
import org.springframework.beans.factory.annotation.Qualifier
import org.springframework.stereotype.Component

@Component
class FreightDocumentController(
        @Qualifier(EventSourcingBeanQualifiers.FREIGHT_DOCUMENT_CONSUMER)
        private val consumerConnector: ConsumerConnector,
        private val service: FreightDocumentService
) : AbstractConsumerController(consumerConnector) {

    @ConsumptionHandler(DocumentSave::class)
    fun saveDocument(document: DocumentSave) {
        val data = document.document.toObject<FreightDocument>()
        service.save(document.documentId, data)
    }

    @ConsumptionHandler(DocumentRemove::class)
    fun removeDocument(document: DocumentRemove) {
        service.delete(document.documentId)
    }
}
