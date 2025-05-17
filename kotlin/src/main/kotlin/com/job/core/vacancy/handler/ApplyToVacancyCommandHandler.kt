package com.job.core.vacancy.handler

import com.job.core.vacancy.command.ApplyToVacancyCommand
import com.job.core.vacancy.dao.VacancyDao
import com.job.library.command.SubjectRegistry
import com.job.library.command.UnitHandler

class ApplyToVacancyCommandHandler(
    private val vacancyDao: VacancyDao,
    private val subjectRegistry: SubjectRegistry,
) : UnitHandler<ApplyToVacancyCommand> {

    override suspend fun handle(command: ApplyToVacancyCommand) {
        val subject = subjectRegistry.getSubject(command.uniqueCommandId)

        vacancyDao.applyUser(
            userId = subject.userId,
            vacancyId = command.vacancyId,
            resumeId = command.resumeId,
        )
    }
}