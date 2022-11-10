package boaentrega.gsl.order

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.data.jpa.repository.config.EnableJpaAuditing
import org.springframework.data.jpa.repository.config.EnableJpaRepositories


@EnableJpaRepositories
@SpringBootApplication()
class DemoApplication

fun main(args: Array<String>) {
    runApplication<DemoApplication>(*args)
}
