package com.job.core.vacancy.handler

import com.job.library.command.UnitHandler
import com.job.core.vacancy.command.CreateVacancyCommand
import com.job.core.vacancy.dao.VacancyDao

class CreateVacancyCommandHandler(
    private val vacancyDao: VacancyDao,
) : UnitHandler<CreateVacancyCommand> {

    override suspend fun handle(command: CreateVacancyCommand) {
        vacancyDao.create(command)
    }
}