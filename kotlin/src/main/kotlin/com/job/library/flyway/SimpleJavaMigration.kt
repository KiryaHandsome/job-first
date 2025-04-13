package com.job.library.flyway

import org.flywaydb.core.api.MigrationVersion
import org.flywaydb.core.api.migration.Context
import org.flywaydb.core.api.migration.JavaMigration

abstract class SimpleJavaMigration(val version: Int = -1) : JavaMigration {

    abstract val query: String

    override fun getVersion(): MigrationVersion = MigrationVersion.fromVersion(this.version.toString())

    override fun getChecksum(): Int? = null // query.hashCode()

    override fun canExecuteInTransaction(): Boolean = true

    override fun migrate(context: Context?) {
        context?.connection?.createStatement()?.execute(query)
    }
}