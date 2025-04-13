package com.job.library.flyway

import com.job.core.migration.ServiceMigrationsProvider
import org.flywaydb.core.Flyway

object FlywayFactory {

    fun configure(url: String, user: String, password: String): Flyway {
        return Flyway.configure()
            .dataSource(url, user, password)
            .javaMigrationClassProvider(ServiceMigrationsProvider())
            .baselineOnMigrate(true)
            .load()
    }
}
