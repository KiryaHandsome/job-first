package com.job.library.command

import kotlin.reflect.KClass
import kotlin.reflect.KType
import kotlin.reflect.KTypeProjection
import kotlin.reflect.full.allSuperclasses
import kotlin.reflect.full.allSupertypes
import kotlin.reflect.full.companionObjectInstance

data class CommandHandlerInfo(
    val commandClass: KClass<Command<*>>,
    val commandHandler: CommandHandler<Command<*>, *>
)

class CommandHandlerRegistry {

    private val uriToCommandHandlerInfo: MutableMap<String, CommandHandlerInfo> = mutableMapOf()

    fun register(handler: CommandHandler<Command<*>, *>) {
        val commandClass = handler.getCommandClass()

        val companionObjectInstance = commandClass.companionObjectInstance
            ?: error("Command ${commandClass.simpleName} should contain object instance")

        if (companionObjectInstance is UriAware) {
            val uri = companionObjectInstance.uri()

            if (uriToCommandHandlerInfo.containsKey(uri)) {
                error("Command ${commandClass.simpleName} already registered as ${companionObjectInstance.uri()}")
            }

            uriToCommandHandlerInfo[uri] = CommandHandlerInfo(commandClass, handler)
        }
    }

    fun findHandlerInfo(uri: String): CommandHandlerInfo? {
        return uriToCommandHandlerInfo[uri]
    }

    private fun CommandHandler<*, *>.getCommandClass(): KClass<Command<*>> {
        this.javaClass.kotlin.allSupertypes.forEach { type: KType ->
            type.arguments.forEach arguments@{ typeProjection: KTypeProjection ->
                val classifier = typeProjection.type?.classifier ?: return@arguments

                classifier as KClass<*>

                @Suppress("UNCHECKED_CAST")
                if (classifier.allSuperclasses.contains(Command::class)) {
                    return classifier as KClass<Command<*>>
                }
            }
        }

        error("No command found for ${this.javaClass.simpleName}")
    }
}
