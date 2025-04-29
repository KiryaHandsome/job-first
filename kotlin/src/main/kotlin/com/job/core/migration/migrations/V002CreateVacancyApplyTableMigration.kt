package com.job.core.migration.migrations

import com.job.library.flyway.SimpleJavaMigration
import org.intellij.lang.annotations.Language

class V002CreateVacancyApplyTableMigration : SimpleJavaMigration(version = 2) {

    override fun getDescription(): String = "Create vacancy apply table"

    @Language("PostgreSQL")
    override val query: String = """
        CREATE TABLE IF NOT EXISTS vacancy_apply(
            vacancy_id UUID NOT NULL,
            user_id UUID NOT NULL,
            PRIMARY KEY (vacancy_id, user_id)
        )
    """.trimIndent()
}