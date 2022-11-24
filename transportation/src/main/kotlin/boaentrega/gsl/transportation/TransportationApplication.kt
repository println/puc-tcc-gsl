package boaentrega.gsl.transportation

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.autoconfigure.amqp.RabbitAutoConfiguration
import org.springframework.boot.runApplication
import org.springframework.data.jpa.repository.config.EnableJpaRepositories

@EnableJpaRepositories
@SpringBootApplication(exclude = [
    RabbitAutoConfiguration::class
])
class DemoApplication

fun main(args: Array<String>) {
    runApplication<DemoApplication>(*args)
}
