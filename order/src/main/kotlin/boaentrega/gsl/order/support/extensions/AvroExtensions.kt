package boaentrega.gsl.order.support.extensions

import boaentrega.gsl.order.support.extensions.ClassExtensions.toJsonString
import org.apache.avro.specific.SpecificRecordBase
import java.lang.reflect.Modifier

object AvroExtensions {
    fun <R : SpecificRecordBase> R.toJsonString(): String {
        val fields = this.javaClass.declaredFields
        fields.forEach { it.isAccessible = true }

        val dataMap = fields
                .filterNot { Modifier.isStatic(it.modifiers) }
                .associate { it.name to it.get(this) }

        return dataMap.toJsonString()
    }
}