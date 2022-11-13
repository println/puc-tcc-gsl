package boaentrega.gsl.order.replicas.order.eventsourcing.document


import boaentrega.gsl.order.configuration.constants.EventSourcingBeanQualifiers
import boaentrega.gsl.order.replicas.order.OrderDocument
import boaentrega.gsl.order.replicas.order.OrderDocumentService
import boaentrega.gsl.order.support.eventsourcing.connectors.ConsumerConnector
import boaentrega.gsl.order.support.eventsourcing.controller.AbstractConsumerController
import boaentrega.gsl.order.support.eventsourcing.controller.annotations.ConsumptionHandler
import boaentrega.gsl.order.support.extensions.ClassExtensions.toObject
import gsl.schemas.DocumentExpired
import gsl.schemas.DocumentReleased

import org.springframework.beans.factory.annotation.Qualifier
import org.springframework.stereotype.Component

@Component
class OrderDocumentController(
        @Qualifier(EventSourcingBeanQualifiers.ORDER_DOCUMENT_CONSUMER)
        private val consumerConnector: ConsumerConnector,
        private val service: OrderDocumentService
) : AbstractConsumerController(consumerConnector) {

    @ConsumptionHandler(DocumentReleased::class)
    fun saveDocument(document: DocumentReleased) {
        val data = document.document.toObject<OrderDocument>()
        service.save(document.documentId, data)
    }

    @ConsumptionHandler(DocumentExpired::class)
    fun removeDocument(document: DocumentExpired) {
        service.delete(document.documentId)
    }
}