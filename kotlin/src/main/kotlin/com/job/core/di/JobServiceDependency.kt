package com.job.core.di

import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.databind.SerializationFeature
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import com.job.library.command.BaseCommand
import com.job.library.command.CommandHandler
import com.job.library.command.CommandHandlerRegistry
import com.job.library.common.db.DatabaseCredentials
import com.job.library.di.autoBind
import com.job.library.http.RpcRouter
import com.job.library.jooq.JooqR2dbcContextFactory
import com.job.library.security.PasswordEncoder
import com.job.core.user.di.userModule
import com.job.core.vacancy.di.vacancyModule
import org.kodein.di.DI
import org.kodein.di.allInstances
import org.kodein.di.bind
import org.kodein.di.direct
import org.kodein.di.singleton


val mainModule = DI.Module("mainModule") {
    bind<CommandHandlerRegistry>() with singleton {
        val registry = CommandHandlerRegistry()

        di.direct.allInstances<CommandHandler<BaseCommand<*>, *>>()
            .forEach { registry.register(it) }

        registry
    }

    autoBind<RpcRouter>()

    bind<ObjectMapper>() with singleton {
        jacksonObjectMapper()
            .registerModule(JavaTimeModule())
            .configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false)
    }

    autoBind<JooqR2dbcContextFactory>()

    bind<DatabaseCredentials>() with singleton {
        // TODO: think of where to store credentials
        DatabaseCredentials(
            userName = "postgres",
            password = "password",
            databaseName = "job_first",
            host = "localhost",
            driver = "postgresql",
            port = 5432,
        )
    }

    bind<PasswordEncoder>() with singleton {
        PasswordEncoder(
            secretKey = "\$2a\$10\$AVPT05HwxGW.2eXHQmzKBO"
        )
    }
}

val di = DI {
    import(mainModule)
    import(vacancyModule)
    import(userModule)
}

