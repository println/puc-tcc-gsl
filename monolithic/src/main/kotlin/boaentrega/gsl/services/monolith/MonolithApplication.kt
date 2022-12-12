package boaentrega.gsl.services.monolith

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.autoconfigure.amqp.RabbitAutoConfiguration
import org.springframework.boot.autoconfigure.domain.EntityScan
import org.springframework.boot.runApplication
import org.springframework.context.annotation.ComponentScan
import org.springframework.context.annotation.FilterType
import org.springframework.data.jpa.repository.config.EnableJpaRepositories


const val CONFIG_PACKAGE = "boaentrega.gsl.configuration"
const val SUPPORT_PACKAGE = "boaentrega.gsl.support"
const val APP_PACKAGE = "boaentrega.gsl.services.monolith"

const val ORDER_PACKAGE = "boaentrega.gsl.services.order"
const val FREIGHT_PACKAGE = "boaentrega.gsl.services.freight"
const val COLLECTION_PACKAGE = "boaentrega.gsl.services.collection"
const val TRANSPORTATION_PACKAGE = "boaentrega.gsl.services.transportation"
const val DELIVERY_PACKAGE = "boaentrega.gsl.services.delivery"

@EntityScan(basePackages = [
    APP_PACKAGE,
    ORDER_PACKAGE,
    FREIGHT_PACKAGE,
    COLLECTION_PACKAGE,
    TRANSPORTATION_PACKAGE,
    DELIVERY_PACKAGE,
    CONFIG_PACKAGE,
    SUPPORT_PACKAGE
])
@ComponentScan(
        basePackages = [
            APP_PACKAGE,
            ORDER_PACKAGE,
            FREIGHT_PACKAGE,
            COLLECTION_PACKAGE,
            TRANSPORTATION_PACKAGE,
            DELIVERY_PACKAGE,
            CONFIG_PACKAGE,
            SUPPORT_PACKAGE
        ],
        excludeFilters = [ComponentScan.Filter(type = FilterType.REGEX, pattern = [".+Application"])]
)
@EnableJpaRepositories(basePackages = [
    APP_PACKAGE,
    ORDER_PACKAGE,
    FREIGHT_PACKAGE,
    COLLECTION_PACKAGE,
    TRANSPORTATION_PACKAGE,
    DELIVERY_PACKAGE,
    CONFIG_PACKAGE,
    SUPPORT_PACKAGE
])
@SpringBootApplication(exclude = [RabbitAutoConfiguration::class])
class MonolithApplication

fun main(args: Array<String>) {
    runApplication<MonolithApplication>(*args)
}
