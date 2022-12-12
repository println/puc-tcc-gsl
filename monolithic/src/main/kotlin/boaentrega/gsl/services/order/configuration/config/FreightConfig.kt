//package boaentrega.gsl.services.order.boaentrega.gsl.services.freight.configuration.config
//
//import boaentrega.gsl.services.order.CONFIG_PACKAGE
//import boaentrega.gsl.services.order.FREIGHT_PACKAGE
//import boaentrega.gsl.services.order.SUPPORT_PACKAGE
//import org.springframework.boot.autoconfigure.EnableAutoConfiguration
//import org.springframework.boot.autoconfigure.amqp.RabbitAutoConfiguration
//import org.springframework.context.annotation.ComponentScan
//import org.springframework.context.annotation.FilterType
//import org.springframework.data.jpa.repository.config.EnableJpaRepositories
//
//@ComponentScan(
//        basePackages = [FREIGHT_PACKAGE, CONFIG_PACKAGE, SUPPORT_PACKAGE],
//        excludeFilters = [ComponentScan.Filter(type = FilterType.REGEX, pattern = [".+Application"])])
//@EnableJpaRepositories(basePackages = [FREIGHT_PACKAGE, CONFIG_PACKAGE, SUPPORT_PACKAGE])
//@EnableAutoConfiguration(exclude = [RabbitAutoConfiguration::class])
//class FreightConfig