package boaentrega.gsl.delivery.configuration.config.evensourcing

import boaentrega.gsl.delivery.support.eventsourcing.connectors.AbstractConnectorFactory
import boaentrega.gsl.delivery.support.eventsourcing.connectors.ProducerConnector
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration


@Configuration
class EventSourcingConfig {
    @Bean
    fun createProducerConnector(connectorFactory: AbstractConnectorFactory): ProducerConnector {
        return connectorFactory.createProducer()
    }
}