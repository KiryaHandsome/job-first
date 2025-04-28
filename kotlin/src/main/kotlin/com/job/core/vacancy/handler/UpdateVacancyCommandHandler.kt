package com.job.core.vacancy.handler

import com.job.core.vacancy.command.UpdateVacancyCommand
import com.job.core.vacancy.dao.VacancyDao
import com.job.library.command.UnitHandler

class UpdateVacancyCommandHandler(
    private val vacancyDao: VacancyDao,
) : UnitHandler<UpdateVacancyCommand> {

    override suspend fun handle(command: UpdateVacancyCommand) {
        vacancyDao.update(command)
    }
}