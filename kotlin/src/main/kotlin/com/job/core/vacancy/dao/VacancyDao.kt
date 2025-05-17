package com.job.core.vacancy.dao

import com.job.core.vacancy.command.EditVacancyCommand
import com.job.core.vacancy.domain.ApplyStatus
import com.job.core.vacancy.domain.ExperienceLevel
import com.job.core.vacancy.domain.Vacancy
import com.job.core.vacancy.domain.VacancyStatus
import com.job.core.vacancy.domain.WorkType
import com.job.core.vacancy.domain.command.CreateVacancyDomainCommand
import com.job.core.vacancy.dto.GetVacanciesFilters
import com.job.core.vacancy.dto.ApplyResumeDto
import com.job.core.vacancy.dto.exception.UserAlreadyAppliedToVacancyException
import com.job.generated.jooq.Tables.USER
import com.job.generated.jooq.tables.Resume.RESUME
import com.job.generated.jooq.tables.UserCompany.USER_COMPANY
import com.job.generated.jooq.tables.Vacancy.VACANCY
import com.job.generated.jooq.tables.VacancyApply.VACANCY_APPLY
import com.job.library.common.money.Currency
import com.job.library.common.money.Money
import com.job.library.common.pagination.Cursor
import com.job.library.common.pagination.Page
import com.job.library.jooq.JooqR2dbcContextFactory
import com.job.library.jooq.getOrNull
import com.job.library.jooq.mapper.RecordMapper
import org.jooq.impl.DSL
import java.time.Instant
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
            val amount: Long? = it.getOrNull(VACANCY.SALARY_MIN)

            amount?.let { Money(amount = it, currency = currency) }
        },
        salaryMax = salaryCurrency?.let { currency ->
            val amount: Long? = it.getOrNull(VACANCY.SALARY_MAX)

            amount?.let { Money(amount = it, currency = currency) }
        },
        workType = it.get(VACANCY.WORK_TYPE)?.let { type -> WorkType.valueOf(type) },
        location = it.get(VACANCY.LOCATION),
        experienceLevel = it.get(VACANCY.EXPERIENCE_LEVEL)?.let { level -> ExperienceLevel.valueOf(level) },
        createdAtMillis = it.get(VACANCY.CREATED_AT_MILLIS),
        editedAtMillis = it.get(VACANCY.EDITED_AT_MILLIS),
        status = VacancyStatus.valueOf(it.get(VACANCY.STATUS)),
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

    fun getPageWithCursorAndFilter(cursor: Cursor, filters: GetVacanciesFilters): Page<Vacancy> {
        return jooqR2dbcContextFactory.fetchPageAndAwait(
            cursor = cursor,
            mapper = vacancyMapper,
            table = VACANCY,
            fields = vacancyFields,
            whereConditions = listOfNotNull(
                VACANCY.STATUS.eq(VacancyStatus.ACTIVE.name), // fetching only ACTIVE vacancies
                filters.currency?.let { VACANCY.SALARY_CURRENCY.eq(it.name) },
                filters.workType?.let { VACANCY.WORK_TYPE.eq(it.name) },
                filters.experience?.let { VACANCY.EXPERIENCE_LEVEL.eq(it.name) },
                filters.salaryFrom?.let { VACANCY.SALARY_MAX.isNull.or(VACANCY.SALARY_MAX.ge(it.toLong())) },

                // todo: implement full text search
                filters.search?.let {
                    VACANCY.DESCRIPTION.likeIgnoreCase("%$it%")
                        .or(VACANCY.TITLE.likeIgnoreCase("%$it%"))
                },
            )
        )
    }

    fun create(command: CreateVacancyDomainCommand) {
        jooqR2dbcContextFactory.use {
            insertInto(VACANCY)
                .set(VACANCY.ID, command.id)
                .set(VACANCY.STATUS, command.status.name)
                .set(VACANCY.TITLE, command.title)
                .set(VACANCY.DESCRIPTION, command.description)
                .set(VACANCY.LOCATION, command.location)
                .set(VACANCY.SALARY_MIN, command.salaryMin?.amount)
                .set(VACANCY.SALARY_MAX, command.salaryMax?.amount)
                .set(VACANCY.SALARY_CURRENCY, command.salaryMin?.currency?.name)
                .set(VACANCY.WORK_TYPE, command.workType?.name)
                .set(VACANCY.EXPERIENCE_LEVEL, command.experienceLevel?.name)
                .set(VACANCY.EMPLOYER_ID, command.publisher)
                .set(VACANCY.CREATED_AT_MILLIS, command.createdAtMillis)
                .set(VACANCY.EDITED_AT_MILLIS, command.editedAtMillis)
        }
    }

    fun update(command: EditVacancyCommand) {
        jooqR2dbcContextFactory.use {
            update(VACANCY)
                .set(VACANCY.TITLE, command.title)
                .set(VACANCY.SALARY_MAX, command.salaryMax)
                .set(VACANCY.SALARY_MIN, command.salaryMin)
                .set(VACANCY.SALARY_CURRENCY, command.salaryCurrency.name)
                .set(VACANCY.EXPERIENCE_LEVEL, command.experienceLevel.name)
                .set(VACANCY.WORK_TYPE, command.workType.name)
                .set(VACANCY.DESCRIPTION, command.description)
                .set(VACANCY.LOCATION, command.location)
                .where(VACANCY.ID.eq(command.id))
        }
    }

    fun applyUser(vacancyId: UUID, userId: UUID, resumeId: UUID) {
        jooqR2dbcContextFactory.use(
            duplicateFactory = {
                UserAlreadyAppliedToVacancyException(userId = userId, vacancyId = vacancyId)
            }
        ) {
            insertInto(VACANCY_APPLY)
                .set(VACANCY_APPLY.VACANCY_ID, vacancyId)
                .set(VACANCY_APPLY.USER_ID, userId)
                .set(VACANCY_APPLY.RESUME_ID, resumeId)
                .set(VACANCY_APPLY.APPLIED_AT_MILLIS, Instant.now().toEpochMilli())
        }
    }

    fun getUserApplies(userId: UUID): Set<UUID> {
        return jooqR2dbcContextFactory.fetchManyAndAwait(
            mapper = {
                it.get(VACANCY_APPLY.VACANCY_ID)
            }
        ) {
            select(VACANCY_APPLY.VACANCY_ID)
                .from(VACANCY_APPLY)
                .where(VACANCY_APPLY.USER_ID.eq(userId))
        }.toSet()
    }

    fun getEmployerVacanciesByUserId(userId: UUID): List<Vacancy> {
        return jooqR2dbcContextFactory.fetchManyAndAwait(mapper = vacancyMapper) {
            select(DSL.asterisk())
                .from(VACANCY)
                .innerJoin(USER_COMPANY)
                .on(USER_COMPANY.COMPANY_ID.eq(VACANCY.EMPLOYER_ID))
                .where(USER_COMPANY.USER_ID.eq(userId))
                .orderBy(VACANCY.CREATED_AT_MILLIS.desc())
        }
    }

    fun getVacancyAppliesCount(vacancyId: UUID): Int {
        return jooqR2dbcContextFactory.fetchOneAndAwaitNullable(
            mapper = { it.get("count", Int::class.java) }
        ) {
            selectCount()
                .from(VACANCY_APPLY)
                .where(VACANCY_APPLY.VACANCY_ID.eq(vacancyId))
        } ?: 0
    }

    fun getVacancyApplies(vacancyId: UUID): List<ApplyResumeDto> {
        return jooqR2dbcContextFactory.fetchManyAndAwait(
            mapper = {
                ApplyResumeDto(
                    id = it.get(VACANCY_APPLY.ID),
                    resumeId = it.get(VACANCY_APPLY.RESUME_ID),
                    resumeTitle = it.get(RESUME.TITLE),
                    firstName = it.get(USER.FIRST_NAME),
                    lastName = it.get(USER.LAST_NAME),
                    appliedAt = it.getOrNull(VACANCY_APPLY.APPLIED_AT_MILLIS),
                    status = ApplyStatus.valueOf(it.get(VACANCY_APPLY.STATUS)),
                )
            }
        ) {
            select(DSL.asterisk())
                .from(VACANCY_APPLY)
                .join(RESUME).on(RESUME.ID.eq(VACANCY_APPLY.RESUME_ID))
                .join(USER).on(USER.ID.eq(VACANCY_APPLY.USER_ID))
                .where(VACANCY_APPLY.VACANCY_ID.eq(vacancyId))
        }
    }

    fun editStatus(id: UUID, status: VacancyStatus) {
        jooqR2dbcContextFactory.use {
            update(VACANCY)
                .set(VACANCY.STATUS, status.name)
                .where(VACANCY.ID.eq(id))
        }
    }

    fun editApplyStatus(id: UUID, status: ApplyStatus) {
        jooqR2dbcContextFactory.use {
            update(VACANCY_APPLY)
                .set(VACANCY_APPLY.STATUS, status.name)
                .where(VACANCY_APPLY.ID.eq(id))
        }
    }
}