package boaentrega.gsl.order.configuration.config.evensourcing

import boaentrega.gsl.support.eventsourcing.connectors.AbstractConnectorFactory
import boaentrega.gsl.support.eventsourcing.connectors.ProducerConnector
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration


@Configuration
class EventSourcingConfig {
    @Bean
    fun createProducerConnector(connectorFactory: boaentrega.gsl.support.eventsourcing.connectors.AbstractConnectorFactory): boaentrega.gsl.support.eventsourcing.connectors.ProducerConnector {
        return connectorFactory.createProducer()
    }
}