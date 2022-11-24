package boaentrega.gsl.transportation.configuration.config.evensourcing

import boaentrega.gsl.transportation.support.eventsourcing.connectors.AbstractConnectorFactory
import boaentrega.gsl.transportation.support.eventsourcing.connectors.ProducerConnector
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration


@Configuration
class EventSourcingConfig {
    @Bean
    fun createProducerConnector(connectorFactory: AbstractConnectorFactory): ProducerConnector {
        return connectorFactory.createProducer()
    }
}