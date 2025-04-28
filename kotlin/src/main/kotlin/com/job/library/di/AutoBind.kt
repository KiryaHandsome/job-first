package com.job.library.di

import org.kodein.di.DI
import org.kodein.di.DirectDI
import org.kodein.di.bindings.NoArgBindingDI
import org.kodein.di.bindings.Singleton
import org.kodein.di.instance
import org.kodein.type.TypeToken
import org.kodein.type.erased
import org.kodein.type.typeToken
import kotlin.reflect.KClass
import kotlin.reflect.full.declaredMembers
import kotlin.reflect.full.primaryConstructor
import kotlin.reflect.full.valueParameters
import kotlin.reflect.jvm.javaType


public typealias DepsFactory = DirectDI.() -> Dependency

public val NullDependencyFactory: DepsFactory = { Dependency() }

public typealias OnCreatedHook<T> = (NoArgBindingDI<*>, T) -> T

public val NullOnCreatedHook: OnCreatedHook<*> = { _, inst -> inst }

public typealias DepsTransformer = NoArgBindingDI<*>.(DepsFactory) -> DepsFactory

public val NullDepsTransformer: DepsTransformer = { deps -> deps }

public inline fun <reified T : Any> DI.Builder.autoBind(
    tag: Any? = null,
    overrides: Boolean? = null,
    noinline usingDependency: DepsFactory = NullDependencyFactory,
    noinline onCreated: OnCreatedHook<T> = { _, inst -> inst },
): Unit = autoBind(base = T::class, impl = T::class, tag, overrides, usingDependency, onCreated)

public inline fun <reified Base : Any, reified Impl : Base> DI.Builder.autoBindBy(
    tag: Any? = null,
    overrides: Boolean? = null,
    noinline usingDependency: DepsFactory = NullDependencyFactory,
    noinline onCreated: OnCreatedHook<Impl> = { _, inst -> inst },
): Unit = autoBind(base = Base::class, impl = Impl::class, tag, overrides, usingDependency, onCreated)

public fun <Base : Any> DI.Builder.autoBind(clazz: KClass<Base>): Unit = autoBind(base = clazz, impl = clazz)

public inline fun <reified Deps : Any, reified T : Any> DI.Builder.autoBindWith(
    tag: Any? = null,
    overrides: Boolean? = null,
    noinline usingDependency: DepsFactory = NullDependencyFactory,
    noinline onCreated: OnCreatedHook<T> = { _, inst -> inst },
): Unit = autoBind(
    base = T::class,
    impl = T::class,
    tag = tag,
    overrides = overrides,
    usingDependency = usingDependency,
    onCreated = onCreated,
    transformDeps = { predefinedDeps ->
        val objConstructor = T::class.primaryConstructor ?: return@autoBind predefinedDeps

        val depsFields = Deps::class.declaredMembers.mapTo(HashSet()) { it.name }
        val objFields = objConstructor.parameters.mapNotNullTo(HashSet()) { it.name }

        val intersection = depsFields.intersect(objFields)

        val newDeps = with(instance<Deps>()) {
            intersection
                .map { fieldName ->
                    fieldName to Deps::class.declaredMembers.first { it.name == fieldName }.call(this)!!
                }
        }

        return@autoBind {
            predefinedDeps().merge(
                Dependency(*newDeps.toTypedArray()),
            )
        }
    },
)

public fun <Base : Any, Impl : Base> DI.Builder.autoBind(
    base: KClass<Base>,
    impl: KClass<Impl>,
    tag: Any? = null,
    overrides: Boolean? = null,
    usingDependency: DepsFactory = NullDependencyFactory,
    onCreated: OnCreatedHook<Impl> = { _, inst -> inst },
    transformDeps: DepsTransformer = NullDepsTransformer,
) {
    Bind(
        tag,
        overrides,
        Singleton(
            scope = scope,
            contextType = contextType,
            explicitContext = explicitContext,
            createdType = erased(base),
            refMaker = null,
            sync = true,
            creator = {
                val instance = directAutoBind(
                    base = base,
                    impl = impl,
                    usingDependency = transformDeps(usingDependency),
                )

                onCreated(
                    this,
                    instance
//                     avoid Dependency recursion:
//                    if (base == Advisor::class) instance else withAdvices(instance),
                )
            },
        ),
    )
}

public inline fun <reified T : Any> DirectDI.directAutoBind(
    noinline usingDependency: DepsFactory = NullDependencyFactory,
): T = directAutoBind(base = T::class, impl = T::class, usingDependency = usingDependency)

public fun <Base : Any, Impl : Base> DirectDI.directAutoBind(
    base: KClass<Base>,
    impl: KClass<Impl>,
    usingDependency: DepsFactory = NullDependencyFactory,
): Impl {
    val globalDependency = InstanceOrNull(erased(Dependency::class)) ?: Dependency()

    val localDependency = usingDependency(this)

    // when autoBind is used for objects
    if (impl.primaryConstructor == null && impl.objectInstance != null) return impl.objectInstance!!

    return impl.primaryConstructor!!.let { ctr ->
        val ctrPropertyNames = ctr.valueParameters.mapTo(HashSet()) { it.name }

        val notUsed = localDependency.names - ctrPropertyNames

        if (notUsed.isNotEmpty()) {
            error("Local dependency for service ${impl.simpleName} provided, but not declared in constructor $notUsed.")
        }

        ctr.callBy(
            buildMap(ctr.valueParameters.size) {
                ctr.valueParameters
                    .forEach { property ->
                        val value = try {
                            val propertyName = property.name!!

                            @Suppress("UNCHECKED_CAST")
                            val type = property.type.classifier as KClass<Any>?

                            if (type != null) {
                                if (localDependency.hasDefault(propertyName, type)) {
                                    put(property, localDependency.getDefault(propertyName))

                                    return@forEach
                                }

                                if (globalDependency.hasDefault(propertyName, type)) {
                                    put(property, globalDependency.getDefault(propertyName))

                                    return@forEach
                                }
                            }

                            @Suppress("UNCHECKED_CAST")
                            val token = typeToken(property.type.javaType) as TypeToken<Any>

                            directDI.Instance(token)
                        } catch (t: Throwable) {
                            // if property is optional and value not resolved use default value
                            if (property.isOptional) return@forEach

                            throw RuntimeException(
                                "AutoBind for class ${base.simpleName} failed. " +
                                        "Property with name ${property.name} not resolved. Throwable: ${t.message}",
                                t,
                            )
                        }

                        put(property, value)
                    }
            },
        )
    }
}