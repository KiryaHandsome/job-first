package com.job.core.migration.migrations

import com.job.library.flyway.SimpleJavaMigration
import org.intellij.lang.annotations.Language

class V003CreateCompanyTablesMigration : SimpleJavaMigration(version = 3) {

    override fun getDescription(): String = "Create company tables"

    @Language("PostgreSQL")
    override val query: String = """
        CREATE TABLE IF NOT EXISTS company (
            id UUID PRIMARY KEY,
            name TEXT NOT NULL UNIQUE,
            employees_count INT,
            description TEXT,
            address TEXT,
            website_link TEXT
        );
        
        -- table to match employer users and company
        CREATE TABLE IF NOT EXISTS user_company (
            user_id UUID NOT NULL PRIMARY KEY,
            company_id UUID NOT NULL
        )
    """.trimIndent()
}
