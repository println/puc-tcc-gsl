package boaentrega.gsl.order.configuration.config

import boaentrega.gsl.order.support.eventsourcing.scope.EventSourcingBeanFactoryPostProcessor
import boaentrega.gsl.order.support.eventsourcing.scope.EventSourcingScope
import boaentrega.gsl.order.support.eventsourcing.scope.EventSourcingScopeConstants
import org.springframework.beans.factory.config.BeanFactoryPostProcessor
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.context.annotation.Scope

@Configuration
class EventSourcingScopeConfig {
    @Bean
    fun beanFactoryPostProcessor(): BeanFactoryPostProcessor {
        return EventSourcingBeanFactoryPostProcessor()
    }
//
//    @Bean
//    @Scope(EventSourcingScopeConstants.SCOPE_NAME)
//    fun eventSourcingScopeContext(): EventSourcingScope {
//        return EventSourcingScope()
//    }
}