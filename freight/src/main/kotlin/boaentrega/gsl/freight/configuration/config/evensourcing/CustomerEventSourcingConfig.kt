package boaentrega.gsl.freight.configuration.config.evensourcing

import boaentrega.gsl.freight.configuration.constants.EventSourcingBeanQualifiers
import boaentrega.gsl.freight.support.eventsourcing.connectors.AbstractConnectorFactory
import boaentrega.gsl.freight.support.eventsourcing.connectors.ConsumerConnector
import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration


@Configuration
class CustomerEventSourcingConfig {

    @Value("\${eventsourcing.messaging.customer.document}")
    private lateinit var customerDocumentTarget: String

    @Bean(EventSourcingBeanQualifiers.CUSTOMER_DOCUMENT_CONSUMER)
    fun createCustomerDocumentConsumerConnector(connectorFactory: AbstractConnectorFactory): ConsumerConnector {
        return connectorFactory.createConsumer(customerDocumentTarget)
    }
}