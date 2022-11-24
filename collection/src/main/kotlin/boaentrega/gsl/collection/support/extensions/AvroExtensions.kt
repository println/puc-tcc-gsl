package boaentrega.gsl.collection.support.extensions

import boaentrega.gsl.collection.support.extensions.ClassExtensions.toJsonString
import org.apache.avro.specific.SpecificRecordBase
import java.lang.reflect.Modifier

object AvroExtensions {
    fun <R : SpecificRecordBase> R.toJsonString(): String {
        val fields = this.javaClass.declaredFields
        return fields
                .filterNot { Modifier.isStatic(it.modifiers) }
                .associate { it.name to it.apply { isAccessible = true }.get(this) }
                .toJsonString()
    }
}