package boaentrega.gsl.services.monolith.snapshots.freight.document


//@Component
//class FreightDocumentController(
//        @Qualifier(EventSourcingBeanQualifiers.FREIGHT_DOCUMENT_CONSUMER)
//        private val consumerConnector: ConsumerConnector,
//        private val service: FreightDocService
//) : AbstractConsumerController(consumerConnector) {
//
//    @ConsumptionHandler(DocumentReleased::class)
//    fun saveDocument(document: DocumentReleased) {
//        val data = document.document.toObject<FreightDocument>()
//        service.save(document.documentId, data)
//    }
//
//    @ConsumptionHandler(DocumentExpired::class)
//    fun removeDocument(document: DocumentExpired) {
//        service.delete(document.documentId)
//    }
//}
