package com.job.library.jooq

import org.jooq.codegen.GenerationTool
import org.jooq.meta.jaxb.Configuration
import org.jooq.meta.jaxb.Database
import org.jooq.meta.jaxb.Generator
import org.jooq.meta.jaxb.Jdbc
import org.jooq.meta.jaxb.Target


class JooqCodegen {

    fun configuration(): Configuration = Configuration()
        .withJdbc(
            Jdbc()
                .withDriver("org.postgresql.Driver")
                .withUrl("jdbc:postgresql:job_first")
                .withUser("postgres")
                .withPassword("password")
        )
        .withGenerator(
            Generator()
                .withDatabase(
                    Database()
                        .withName("org.jooq.meta.postgres.PostgresDatabase")
                        .withIncludes(".*")
                        .withExcludes("")
                        .withInputSchema("public")
                )
                .withTarget(
                    Target()
                        .withPackageName("com.job.generated.jooq")
                        .withDirectory("kotlin/src/main/java")
                )
        )
}

fun main() {
    val codeGen = JooqCodegen()

    val config = codeGen.configuration()

    GenerationTool.generate(config);
}