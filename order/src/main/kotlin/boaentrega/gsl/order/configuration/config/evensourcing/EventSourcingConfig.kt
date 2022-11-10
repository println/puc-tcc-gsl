package boaentrega.gsl.order.configuration.config.evensourcing

import boaentrega.gsl.order.support.eventsourcing.connectors.AbstractConnectorFactory
import boaentrega.gsl.order.support.eventsourcing.connectors.ProducerConnector
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration


@Configuration
class EventSourcingConfig {
    @Bean
    fun createProducerConnector(connectorFactory: AbstractConnectorFactory): ProducerConnector {
        return connectorFactory.createProducer()
    }
}