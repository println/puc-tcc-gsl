package boaentrega.gsl.support.functions

import boaentrega.gsl.support.eventsourcing.controller.annotations.ConsumptionHandler
import com.fasterxml.jackson.core.JsonProcessingException
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.module.kotlin.KotlinFeature
import com.fasterxml.jackson.module.kotlin.KotlinModule
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import com.fleshgrinder.extensions.kotlin.toLowerDashCase
import kotlin.reflect.KClass


object Functions {
    object Json {
        private val mapper: ObjectMapper = jacksonObjectMapper()
                .registerModule((KotlinModule.Builder()
                .withReflectionCacheSize(512)
                .configure(KotlinFeature.NullToEmptyCollection, false)
                .configure(KotlinFeature.NullToEmptyMap, false)
                .configure(KotlinFeature.NullIsSameAsDefault, false)
                .configure(KotlinFeature.SingletonSupport, false)
                .configure(KotlinFeature.StrictNullChecks, false)
                .build()))

        fun isValid(json: String): Boolean {
            try {
                mapper.readTree(json)
            } catch (e: JsonProcessingException) {
                return false
            }
            return true
        }
    }

    object Message {
        fun extractIdentifier(clazz: KClass<*>): String {
            return clazz.simpleName.toString().toLowerDashCase()
        }

        fun extractIdentifier(clazz: Class<*>): String {
            return extractIdentifier(clazz.kotlin)
        }

        fun extractIdentifier(data: Any): String {
            return extractIdentifier(data::class)
        }

        fun extractIdentifier(annotation: ConsumptionHandler): String {
            return extractIdentifier(annotation.handleClass)
        }
    }
}