package com.job.core.vacancy.handler

import com.job.core.vacancy.command.CreateVacancyCommand
import com.job.core.vacancy.dao.VacancyDao
import com.job.core.vacancy.domain.command.CreateVacancyDomainCommand
import com.job.library.command.UnitHandler
import com.job.library.common.uuid.Uuid
import java.time.Instant
import java.util.UUID

class CreateVacancyCommandHandler(
    private val vacancyDao: VacancyDao,
) : UnitHandler<CreateVacancyCommand> {

    override suspend fun handle(command: CreateVacancyCommand) {
        val time = Instant.now().toEpochMilli()

        val domainCommand = CreateVacancyDomainCommand(
            id = Uuid.v7(),
            publisher = UUID.randomUUID(), // get from token?
            title = command.title,
            salaryMin = command.salaryMin,
            salaryMax = command.salaryMax,
            workType = command.workType,
            location = command.location,
            description = command.description,
            experienceLevel = command.experienceLevel,
            viewsCount = 0,
            createdAtMillis = time,
            editedAtMillis = time
        )

        vacancyDao.create(domainCommand)
    }
}