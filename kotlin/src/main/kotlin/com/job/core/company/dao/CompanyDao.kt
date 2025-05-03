package com.job.core.company.dao

import com.fasterxml.jackson.databind.ObjectMapper
import com.job.core.company.domain.Company
import com.job.core.company.domain.command.CreateCompanyDomainCommand
import com.job.core.company.domain.command.EditCompanyDomainCommand
import com.job.generated.jooq.tables.Company.COMPANY
import com.job.generated.jooq.tables.UserCompany.USER_COMPANY
import com.job.library.jooq.JooqR2dbcContextFactory
import com.job.library.jooq.getOrNull
import org.jooq.impl.DSL
import java.util.UUID

class CompanyDao(
    private val objectMapper: ObjectMapper,
    private val jooqR2dbcContextFactory: JooqR2dbcContextFactory,
) {

    fun findCompanyByEmployerId(employerId: UUID): Company? {
        return jooqR2dbcContextFactory.fetchOneAndAwaitNullable(
            mapper = {
                Company(
                    id = it.get(COMPANY.ID),
                    name = it.get(COMPANY.NAME),
                    employeesCount = it.getOrNull(COMPANY.EMPLOYEES_COUNT),
                    description = it.getOrNull(COMPANY.DESCRIPTION),
                    address = it.getOrNull(COMPANY.ADDRESS),
                    websiteLink = it.getOrNull(COMPANY.WEBSITE_LINK),
                )
            }
        ) {

            select(DSL.asterisk())
                .from(COMPANY)
                .join(USER_COMPANY).on(USER_COMPANY.COMPANY_ID.eq(COMPANY.ID))
                .where(USER_COMPANY.USER_ID.eq(employerId))
        }
    }

    fun create(cmd: CreateCompanyDomainCommand) {
        jooqR2dbcContextFactory.use {
            insertInto(COMPANY)
                .set(COMPANY.ID, cmd.id)
                .set(COMPANY.NAME, cmd.name)
                .set(COMPANY.DESCRIPTION, cmd.description)
                .set(COMPANY.EMPLOYEES_COUNT, cmd.employeesCount)
                .set(COMPANY.ADDRESS, cmd.address)
        }
    }

    fun edit(cmd: EditCompanyDomainCommand) {
        jooqR2dbcContextFactory.use {
            update(COMPANY)
                .set(COMPANY.NAME, cmd.name)
                .set(COMPANY.DESCRIPTION, cmd.description)
                .set(COMPANY.EMPLOYEES_COUNT, cmd.employeesCount)
                .set(COMPANY.ADDRESS, cmd.address)
                .set(COMPANY.WEBSITE_LINK, cmd.websiteLink)
                .where(COMPANY.ID.eq(cmd.id))
        }
    }

    fun registerUserInCompany(userId: UUID, companyId: UUID) {
        jooqR2dbcContextFactory.use {
            insertInto(USER_COMPANY)
                .set(USER_COMPANY.USER_ID, userId)
                .set(USER_COMPANY.COMPANY_ID, companyId)
        }
    }
}