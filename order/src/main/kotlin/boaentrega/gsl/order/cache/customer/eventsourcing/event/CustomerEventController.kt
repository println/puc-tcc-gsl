package boaentrega.gsl.order.cache.customer.eventsourcing.event

//import boaentrega.gsl.order.cache.customer.Customer
//import boaentrega.gsl.order.cache.customer.CustomerService
//import boaentrega.gsl.order.configuration.constants.EventSourcingBeansConstants
//import boaentrega.gsl.order.support.eventsourcing.connectors.ConsumerConnector
//import gsl.schemas.CustomerCreatedEvent
//import gsl.schemas.CustomerDeletedEvent
//import gsl.schemas.CustomerUpdatedEvent
//import org.springframework.beans.factory.annotation.Qualifier
//import org.springframework.stereotype.Component
//import java.util.*

////@Component
//class CustomerEventController(
//        @Qualifier(EventSourcingBeansConstants.CONTEXT_EVENT_CONSUMER)
//        private val consumerConnector: ConsumerConnector,
//        private val service: CustomerService
//) : AbstractConsumerController(consumerConnector) {
//
//    @ConsumptionHandler(CustomerCreatedEvent::class)
//    fun listenCustomerCreated(event: CustomerCreatedEvent) {
//        service.create(Customer(UUID.randomUUID()))
//    }
//
//    @ConsumptionHandler(CustomerUpdatedEvent::class)
//    fun listenCustomerUpdated(event: CustomerUpdatedEvent) {
//        service.update(UUID.randomUUID(), Customer(UUID.randomUUID()))
//    }
//
//    @ConsumptionHandler(CustomerDeletedEvent::class)
//    fun listenCustomerDeleted(event: CustomerDeletedEvent) {
//        service.delete(UUID.randomUUID())
//    }
//}