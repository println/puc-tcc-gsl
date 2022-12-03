package boaentrega.gsl.order.configuration.config.evensourcing

import boaentrega.gsl.order.configuration.constants.EventSourcingBeanQualifiers
import boaentrega.gsl.support.eventsourcing.connectors.AbstractConnectorFactory
import boaentrega.gsl.support.eventsourcing.connectors.ConsumerConnector
import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration


@Configuration
class CustomerEventSourcingConfig {

    @Value("\${eventsourcing.messaging.customer.document}")
    private lateinit var customerDocumentTarget: String

    @Bean(EventSourcingBeanQualifiers.CUSTOMER_DOCUMENT_CONSUMER)
    fun createCustomerDocumentConsumerConnector(connectorFactory: boaentrega.gsl.support.eventsourcing.connectors.AbstractConnectorFactory): boaentrega.gsl.support.eventsourcing.connectors.ConsumerConnector {
        return connectorFactory.createConsumer(customerDocumentTarget)
    }
}