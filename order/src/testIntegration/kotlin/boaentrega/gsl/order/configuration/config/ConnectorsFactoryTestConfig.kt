package boaentrega.gsl.order.configuration.config

import boaentrega.gsl.order.support.eventsourcing.connectors.dummy.DummyConnectorFactory
import org.springframework.boot.test.context.TestConfiguration
import org.springframework.context.annotation.Bean


@TestConfiguration
class ConnectorsFactoryTestConfig {

    @Bean
    fun defineConnectorsFactory(): boaentrega.gsl.order.support.eventsourcing.connectors.AbstractConnectorFactory {
        return DummyConnectorFactory()
    }
}