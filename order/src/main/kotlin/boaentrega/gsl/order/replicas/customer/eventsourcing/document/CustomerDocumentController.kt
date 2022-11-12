package boaentrega.gsl.order.replicas.customer.eventsourcing.document


import boaentrega.gsl.order.configuration.constants.EventSourcingBeanQualifiers
import boaentrega.gsl.order.replicas.customer.CustomerDocument
import boaentrega.gsl.order.replicas.customer.CustomerDocumentService
import boaentrega.gsl.order.support.eventsourcing.connectors.ConsumerConnector
import boaentrega.gsl.order.support.eventsourcing.controller.AbstractConsumerController
import boaentrega.gsl.order.support.eventsourcing.controller.annotations.ConsumptionHandler
import boaentrega.gsl.order.support.extensions.ClassExtensions.toObject
import gsl.schemas.DocumentExpired
import gsl.schemas.DocumentReleased
import org.springframework.beans.factory.annotation.Qualifier
import org.springframework.stereotype.Component

@Component
class CustomerDocumentController(
        @Qualifier(EventSourcingBeanQualifiers.CUSTOMER_DOCUMENT_CONSUMER)
        private val consumerConnector: ConsumerConnector,
        private val service: CustomerDocumentService
) : AbstractConsumerController(consumerConnector) {

    @ConsumptionHandler(DocumentReleased::class)
    fun saveDocument(document: DocumentReleased) {
        val data = document.document.toObject<CustomerDocument>()
        service.save(document.documentId, data)
    }

    @ConsumptionHandler(DocumentExpired::class)
    fun removeDocument(document: DocumentExpired) {
        service.delete(document.documentId)
    }
}
