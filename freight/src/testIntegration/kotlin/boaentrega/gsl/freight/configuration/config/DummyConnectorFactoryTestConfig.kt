package boaentrega.gsl.freight.configuration.config

import boaentrega.gsl.freight.support.eventsourcing.connectors.AbstractConnectorFactory
import boaentrega.gsl.freight.support.eventsourcing.connectors.dummy.DummyConnectorFactory
import org.springframework.boot.test.context.TestConfiguration
import org.springframework.context.annotation.Bean


@TestConfiguration
class DummyConnectorFactoryTestConfig {

    @Bean
    fun defineConnectorsFactory(): AbstractConnectorFactory {
        return DummyConnectorFactory()
    }
}