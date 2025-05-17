package com.job.core.di

import com.fasterxml.jackson.databind.DeserializationFeature
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.databind.SerializationFeature
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import com.job.core.company.di.companyModule
import com.job.core.email.di.emailModule
import com.job.core.resume.di.resumeModule
import com.job.core.user.di.userModule
import com.job.core.user.service.TokenManager
import com.job.core.vacancy.di.vacancyModule
import com.job.library.command.Command
import com.job.library.command.CommandHandler
import com.job.library.command.CommandHandlerRegistry
import com.job.library.command.SubjectRegistry
import com.job.library.common.db.DatabaseCredentials
import com.job.library.common.security.JwtInfo
import com.job.library.di.autoBind
import com.job.library.http.RpcRouter
import com.job.library.http.middleware.AuthenticationMiddleware
import com.job.library.http.middleware.Middleware
import com.job.library.http.middleware.MiddlewareRegistry
import com.job.library.jooq.JooqR2dbcContextFactory
import com.job.library.security.PasswordEncoder
import org.kodein.di.DI
import org.kodein.di.allInstances
import org.kodein.di.bind
import org.kodein.di.direct
import org.kodein.di.singleton


val mainModule = DI.Module("mainModule") {
    bind<CommandHandlerRegistry>() with singleton {
        val registry = CommandHandlerRegistry()

        di.direct.allInstances<CommandHandler<Command<*>, *>>()
            .forEach { registry.register(it) }

        registry
    }

    autoBind<RpcRouter>()
    autoBind<AuthenticationMiddleware>()

    bind<TokenManager>() with singleton {
        // TODO: store secret in env
        TokenManager(
            jwtInfo = JwtInfo(
                issuer = "com.job.first",
                audience = "audience",
                secret = "supersecret"
            ),
        )
    }

    bind<MiddlewareRegistry>() with singleton {
        MiddlewareRegistry(
            allInstances<Middleware>()
        )
    }


    bind<ObjectMapper>() with singleton {
        jacksonObjectMapper()
            .registerModule(JavaTimeModule())
            .configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false)
            .configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false)
    }

    autoBind<JooqR2dbcContextFactory>()

    autoBind<SubjectRegistry>()

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
        // TODO: store secret
        PasswordEncoder(
            secretKey = "\$2a\$10\$AVPT05HwxGW.2eXHQmzKBO"
        )
    }


}

val di = DI {
    import(mainModule)
    import(vacancyModule)
    import(userModule)
    import(resumeModule)
    import(companyModule)
    import(emailModule)
}

