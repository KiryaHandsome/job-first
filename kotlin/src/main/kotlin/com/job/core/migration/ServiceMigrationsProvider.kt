package com.job.core.migration

import com.job.core.migration.migrations.V001InitialSchemaMigration
import com.job.core.migration.migrations.V002CreateVacancyApplyTableMigration
import org.flywaydb.core.api.ClassProvider
import org.flywaydb.core.api.migration.JavaMigration

class ServiceMigrationsProvider : ClassProvider<JavaMigration> {

    override fun getClasses(): Collection<Class<out JavaMigration?>?>? = listOf(
        V001InitialSchemaMigration::class.java,
        V002CreateVacancyApplyTableMigration::class.java,
    )

}