package boaentrega.gsl.collection.replicas.customer.eventsourcing.document


import boaentrega.gsl.collection.configuration.constants.EventSourcingBeanQualifiers
import boaentrega.gsl.collection.replicas.customer.CustomerDoc
import boaentrega.gsl.collection.replicas.customer.CustomerDocService
import boaentrega.gsl.collection.support.eventsourcing.connectors.ConsumerConnector
import boaentrega.gsl.collection.support.eventsourcing.controller.AbstractConsumerController
import boaentrega.gsl.collection.support.eventsourcing.controller.annotations.ConsumptionHandler
import boaentrega.gsl.collection.support.extensions.ClassExtensions.toObject
import gsl.schemas.DocumentExpired
import gsl.schemas.DocumentReleased
import org.springframework.beans.factory.annotation.Qualifier
import org.springframework.stereotype.Component

@Component
class CustomerDocumentController(
        @Qualifier(EventSourcingBeanQualifiers.CUSTOMER_DOCUMENT_CONSUMER)
        private val consumerConnector: ConsumerConnector,
        private val service: CustomerDocService
) : AbstractConsumerController(consumerConnector) {

    @ConsumptionHandler(DocumentReleased::class)
    fun saveDocument(document: DocumentReleased) {
        val data = document.document.toObject<CustomerDoc>()
        service.save(document.documentId, data)
    }

    @ConsumptionHandler(DocumentExpired::class)
    fun removeDocument(document: DocumentExpired) {
        service.delete(document.documentId)
    }
}
