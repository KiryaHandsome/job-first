package com.job.core.vacancy.handler

import com.job.core.vacancy.command.GetEmployerVacanciesQuery
import com.job.core.vacancy.dao.VacancyDao
import com.job.core.vacancy.domain.Vacancy
import com.job.library.command.CommandHandler
import com.job.library.command.SubjectRegistry

class GetEmployerVacanciesQueryHandler(
    private val vacancyDao: VacancyDao,
    private val subjectRegistry: SubjectRegistry,
) : CommandHandler<GetEmployerVacanciesQuery, List<Vacancy>> {

    override suspend fun handle(command: GetEmployerVacanciesQuery): List<Vacancy> {
        val subject = subjectRegistry.getSubject(commandId = command.uniqueCommandId)

        return vacancyDao.getEmployerVacancies(employerId = subject.userId)
    }
}