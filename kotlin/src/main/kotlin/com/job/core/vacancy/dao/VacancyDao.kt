package com.job.core.vacancy.dao

import com.job.library.common.pagination.Cursor
import com.job.library.common.pagination.Page
import com.job.library.common.uuid.Uuid
import com.job.library.jooq.JooqR2dbcContextFactory
import com.job.library.jooq.mapper.RecordMapper
import com.job.generated.jooq.tables.Vacancy.VACANCY
import com.job.core.vacancy.command.CreateVacancyCommand
import com.job.core.vacancy.command.UpdateVacancyCommand
import com.job.core.vacancy.domain.ExperienceLevel
import com.job.core.vacancy.domain.Vacancy
import com.job.core.vacancy.domain.WorkType
import com.job.core.vacancy.domain.command.CreateVacancyDomainCommand
import com.job.library.common.money.Currency
import com.job.library.common.money.Money
import com.job.library.jooq.findEnumValue
import java.util.UUID


private val vacancyMapper: RecordMapper<Vacancy> = {
    val salaryCurrency = it.get(VACANCY.SALARY_CURRENCY)
        ?.let { currency -> Currency.valueOf(currency) }

    Vacancy(
        id = it.get(VACANCY.ID),
        title = it.get(VACANCY.TITLE),
        description = it.get(VACANCY.DESCRIPTION),
        publisher = it.get(VACANCY.EMPLOYER_ID), // todo change to publisher?
        salaryMin = salaryCurrency?.let { currency ->
            Money(amount = it.get(VACANCY.SALARY_MIN), currency = currency)
        },
        salaryMax = salaryCurrency?.let { currency ->
            Money(amount = it.get(VACANCY.SALARY_MIN), currency = currency)
        },
        workType = it.get(VACANCY.WORK_TYPE)?.let { type -> WorkType.valueOf(type) },
        location = it.get(VACANCY.LOCATION),
        experienceLevel = it.get(VACANCY.EXPERIENCE_LEVEL)?.let { level -> ExperienceLevel.valueOf(level) },
        viewsCount = it.get(VACANCY.VIEWS_COUNT),
        createdAtMillis = it.get(VACANCY.CREATED_AT_MILLIS),
        editedAtMillis = it.get(VACANCY.EDITED_AT_MILLIS),
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

    fun create(command: CreateVacancyDomainCommand) {
        jooqR2dbcContextFactory.use {
            insertInto(VACANCY)
                .set(VACANCY.ID, command.id)
                .set(VACANCY.TITLE, command.title)
                .set(VACANCY.DESCRIPTION, command.description)
                .set(VACANCY.LOCATION, command.location)
                .set(VACANCY.SALARY_MIN, command.salaryMin?.amount)
                .set(VACANCY.SALARY_MAX, command.salaryMax?.amount)
                .set(VACANCY.SALARY_CURRENCY, command.salaryMin?.currency?.name)
                .set(VACANCY.WORK_TYPE, command.workType?.name)
                .set(VACANCY.EXPERIENCE_LEVEL, command.experienceLevel?.name)
                .set(VACANCY.VIEWS_COUNT, command.viewsCount)
                .set(VACANCY.EMPLOYER_ID, command.publisher)
                .set(VACANCY.CREATED_AT_MILLIS, command.createdAtMillis)
                .set(VACANCY.EDITED_AT_MILLIS, command.editedAtMillis)
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