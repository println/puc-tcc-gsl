package boaentrega.gsl.order.support.eventsourcing.scope

import org.springframework.beans.factory.ObjectFactory
import org.springframework.beans.factory.config.Scope
import java.util.*
import kotlin.collections.HashMap


class EventSourcingScope : Scope {

    private val scopedObjects: MutableMap<String, Any> = Collections.synchronizedMap(HashMap<String, Any>())
    private val destructionCallbacks: MutableMap<String, Runnable> = Collections.synchronizedMap(HashMap<String, Runnable>())

    override fun get(name: String, objectFactory: ObjectFactory<*>): Any {
        if (!scopedObjects.containsKey(name)) {
            scopedObjects[name] = objectFactory.getObject()
        }
        return scopedObjects[name]!!
    }

    override fun remove(name: String): Any? {
        destructionCallbacks.remove(name)
        return scopedObjects.remove(name)
    }

    override fun registerDestructionCallback(name: String, callback: Runnable) {
        destructionCallbacks[name] = callback
    }

    override fun resolveContextualObject(key: String): Any? {
        return null
    }

    override fun getConversationId(): String {
        return EventSourcingScopeConstants.SCOPE_NAME
    }
}