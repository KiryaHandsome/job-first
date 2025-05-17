package com.job.core.vacancy.handler

import com.job.core.vacancy.command.EditVacancyCommand
import com.job.core.vacancy.dao.VacancyDao
import com.job.library.command.UnitHandler

class EditVacancyCommandHandler(
    private val vacancyDao: VacancyDao,
) : UnitHandler<EditVacancyCommand> {

    override suspend fun handle(command: EditVacancyCommand) {
        vacancyDao.update(command)
    }
}