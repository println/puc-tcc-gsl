package boaentrega.gsl.freight.configuration.config.evensourcing

import boaentrega.gsl.freight.support.eventsourcing.connectors.AbstractConnectorFactory
import boaentrega.gsl.freight.support.eventsourcing.connectors.ProducerConnector
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration


@Configuration
class EventSourcingConfig {
    @Bean
    fun createProducerConnector(connectorFactory: AbstractConnectorFactory): ProducerConnector {
        return connectorFactory.createProducer()
    }
}