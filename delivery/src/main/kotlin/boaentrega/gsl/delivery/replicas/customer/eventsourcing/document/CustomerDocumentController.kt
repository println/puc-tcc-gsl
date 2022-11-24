package boaentrega.gsl.delivery.replicas.customer.eventsourcing.document


import boaentrega.gsl.delivery.configuration.constants.EventSourcingBeanQualifiers
import boaentrega.gsl.delivery.replicas.customer.CustomerDoc
import boaentrega.gsl.delivery.replicas.customer.CustomerDocService
import boaentrega.gsl.delivery.support.eventsourcing.connectors.ConsumerConnector
import boaentrega.gsl.delivery.support.eventsourcing.controller.AbstractConsumerController
import boaentrega.gsl.delivery.support.eventsourcing.controller.annotations.ConsumptionHandler
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
