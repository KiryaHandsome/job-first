package com.job.core.vacancy.dao

import com.job.library.common.pagination.Cursor
import com.job.library.common.pagination.Page
import com.job.library.common.uuid.Uuid
import com.job.library.jooq.JooqR2dbcContextFactory
import com.job.library.jooq.mapper.RecordMapper
import com.job.generated.jooq.tables.Vacancy.VACANCY
import com.job.core.vacancy.command.CreateVacancyCommand
import com.job.core.vacancy.command.UpdateVacancyCommand
import com.job.core.vacancy.domain.Vacancy
import java.util.UUID


private val vacancyMapper: RecordMapper<Vacancy> = {
    Vacancy(
        id = it.get(VACANCY.ID),
        title = it.get(VACANCY.TITLE),
        description = it.get(VACANCY.DESCRIPTION),
        company = "",
        location = "",
        salary = "",
        createdAt = TODO(),
        editedAt = TODO()
    )
}

private val vacancyFields = VACANCY.fields()

class VacancyDao(
    private val jooqR2dbcContextFactory: JooqR2dbcContextFactory
) {

    fun findById(id: UUID): Vacancy? {
        val vacancy = jooqR2dbcContextFactory.fetchOneAndAwaitNullable(
            mapper = vacancyMapper,
        ) {
            select(*vacancyFields)
                .from(VACANCY)
                .where(VACANCY.ID.eq(id))
        }

        return vacancy
    }

    fun getPageWithCursor(cursor: Cursor): Page<Vacancy> {
        return jooqR2dbcContextFactory.fetchPageAndAwait(
            cursor = cursor,
            mapper = vacancyMapper,
            table = VACANCY,
            fields = vacancyFields,
        )
    }

    fun create(command: CreateVacancyCommand) {
        jooqR2dbcContextFactory.use {
            insertInto(VACANCY)
                .set(VACANCY.ID, Uuid.v7()) // todo: move id generation to handler
                .set(VACANCY.TITLE, command.title)
                .set(VACANCY.DESCRIPTION, command.description)
                .set(VACANCY.LOCATION, command.location)
        }
    }

    fun update(command: UpdateVacancyCommand) {
        jooqR2dbcContextFactory.use {
            update(VACANCY)
                .set(VACANCY.TITLE, command.title)
                .set(VACANCY.DESCRIPTION, command.description)
                .set(VACANCY.LOCATION, command.location)
                .where(VACANCY.ID.eq(command.id))
        }
    }
}