package boaentrega.gsl.order.configuration.config

import boaentrega.gsl.order.support.eventsourcing.connectors.AbstractConnectorFactory
import boaentrega.gsl.order.support.eventsourcing.connectors.dummy.DummyConnectorFactory
import boaentrega.gsl.order.support.eventsourcing.connectors.dummy.DummyProducerConnector
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration


@Configuration
@ConditionalOnProperty(prefix = "eventsourcing", name = ["platform"], havingValue = "local")
class DummyConnectorFactoryConfig {

    @Bean
    fun defineConnectorsFactory(): AbstractConnectorFactory {
        DummyProducerConnector.enableConsumption = true
        return DummyConnectorFactory()
    }
}