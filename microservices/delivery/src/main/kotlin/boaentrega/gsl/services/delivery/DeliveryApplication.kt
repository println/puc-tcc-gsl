package boaentrega.gsl.services.delivery

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.autoconfigure.amqp.RabbitAutoConfiguration
import org.springframework.boot.autoconfigure.domain.EntityScan
import org.springframework.boot.runApplication
import org.springframework.data.jpa.repository.config.EnableJpaRepositories

const val APP_PACKAGE = "boaentrega.gsl"

@EntityScan(basePackages = [APP_PACKAGE])
@EnableJpaRepositories(basePackages = [APP_PACKAGE])
@SpringBootApplication(scanBasePackages = [APP_PACKAGE],
        exclude = [RabbitAutoConfiguration::class])
class DeliveryApplication

fun main(args: Array<String>) {
    runApplication<DeliveryApplication>(*args)
}