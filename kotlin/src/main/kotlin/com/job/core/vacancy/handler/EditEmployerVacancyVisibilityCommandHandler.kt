package com.job.core.vacancy.handler

import com.job.core.vacancy.command.EditEmployerVacancyVisibilityCommand
import com.job.core.vacancy.dao.VacancyDao
import com.job.core.vacancy.domain.VacancyStatus
import com.job.library.command.UnitHandler

class EditEmployerVacancyVisibilityCommandHandler(
    private val vacancyDao: VacancyDao,
) : UnitHandler<EditEmployerVacancyVisibilityCommand> {

    override suspend fun handle(command: EditEmployerVacancyVisibilityCommand) {
        vacancyDao.editStatus(id = command.id, status = command.status)
    }
}