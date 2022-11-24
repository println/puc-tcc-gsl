package boaentrega.gsl.freight.support.eventsourcing.controller.annotations

import kotlin.reflect.KClass

@Target(AnnotationTarget.FUNCTION)
annotation class ConsumptionHandler(val handleClass: KClass<*>)

