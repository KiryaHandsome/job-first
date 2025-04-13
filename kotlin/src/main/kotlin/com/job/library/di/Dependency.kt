package com.job.library.di

import org.kodein.di.DI
import org.kodein.di.bind
import org.kodein.di.singleton
import kotlin.reflect.KClass
import kotlin.reflect.KProperty

public class Dependency(
    vararg defaults: Pair<String, Any>,
) {
    public val names: Set<String>
        get() = defaultsMap.keys

    private val defaultsMap = mapOf(*defaults)

    public fun hasDefault(name: String, type: KClass<Any>): Boolean {
        val value = defaultsMap[name] ?: return false

        return type.isInstance(value)
    }

    public fun getDefault(name: String): Any? = defaultsMap[name]

    public fun merge(other: Dependency): Dependency {
        return Dependency(
            *defaultsMap.entries.map { it.toPair() }.toTypedArray(),
            *other.defaultsMap.map { it.toPair() }.toTypedArray(),
        )
    }
}

public inline fun <reified V : Any> KProperty<V>.withDefault(v: V): Dependency = Dependency(name to v)

public fun DI.Builder.autoConfigure(
    vararg defaults: Pair<String, Any>,
) {
    bind { singleton { Dependency(*defaults) } }
}
