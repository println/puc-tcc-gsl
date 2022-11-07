package boaentrega.gsl.order.support.eventsourcing.scope

import org.springframework.beans.BeansException
import org.springframework.beans.factory.config.BeanFactoryPostProcessor
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory


class EventSourcingBeanFactoryPostProcessor : BeanFactoryPostProcessor {
    @Throws(BeansException::class)
    override fun postProcessBeanFactory(factory: ConfigurableListableBeanFactory) {
        factory.registerScope(EventSourcingScopeConstants.SCOPE_NAME, EventSourcingScope())
    }
}