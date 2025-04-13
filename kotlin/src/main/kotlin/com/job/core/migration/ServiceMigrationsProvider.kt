package com.job.core.migration

import com.job.core.migration.migrations.V001InitialSchemaMigration
import org.flywaydb.core.api.ClassProvider
import org.flywaydb.core.api.migration.JavaMigration

class ServiceMigrationsProvider : ClassProvider<JavaMigration> {

    override fun getClasses(): Collection<Class<out JavaMigration?>?>? = listOf(
        V001InitialSchemaMigration::class.java,
    )

}