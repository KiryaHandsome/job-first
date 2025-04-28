package com.job.library.flyway

import com.job.core.di.di
import com.job.library.common.db.DatabaseCredentials
import io.ktor.server.application.*
import org.kodein.di.direct
import org.kodein.di.instance

fun Application.configureFlyway() {
    val dbCredentials = di.direct.instance<DatabaseCredentials>()

    FlywayFactory.configure(dbCredentials.getUrl(), dbCredentials.userName, dbCredentials.password).migrate()
}
