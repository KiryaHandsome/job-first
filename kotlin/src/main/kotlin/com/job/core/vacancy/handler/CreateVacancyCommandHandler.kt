package com.job.core.vacancy.handler

import com.job.core.company.dao.CompanyDao
import com.job.core.vacancy.command.CreateVacancyCommand
import com.job.core.vacancy.dao.VacancyDao
import com.job.core.vacancy.domain.VacancyStatus
import com.job.core.vacancy.domain.command.CreateVacancyDomainCommand
import com.job.library.command.SubjectRegistry
import com.job.library.command.UnitHandler
import com.job.library.common.money.Money
import com.job.library.common.uuid.Uuid
import java.time.Instant

class CreateVacancyCommandHandler(
    private val vacancyDao: VacancyDao,
    private val companyDao: CompanyDao,
    private val subjectRegistry: SubjectRegistry,
) : UnitHandler<CreateVacancyCommand> {

    override suspend fun handle(command: CreateVacancyCommand) {
        val time = Instant.now().toEpochMilli()

        val subject = subjectRegistry.getSubject(command)

        val company = companyDao.findCompanyByEmployerId(employerId = subject.userId) ?: error("User not in company")

        val domainCommand = CreateVacancyDomainCommand(
            id = Uuid.v7(),
            publisher = company.id,
            title = command.title,
            salaryMin = command.salaryMin?.let { Money(amount = it, currency = command.salaryCurrency) },
            salaryMax = command.salaryMax?.let { Money(amount = it, currency = command.salaryCurrency) },
            workType = command.workType,
            location = command.location,
            description = command.description,
            experienceLevel = command.experienceLevel,
            appliesCount = 0,
            createdAtMillis = time,
            editedAtMillis = time,
            status = VacancyStatus.WAITING_FOR_PAYMENT,
        )

        vacancyDao.create(domainCommand)
    }
}