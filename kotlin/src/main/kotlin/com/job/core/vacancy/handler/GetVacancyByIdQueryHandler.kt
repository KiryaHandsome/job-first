package com.job.core.vacancy.handler

import com.job.library.command.CommandHandler
import com.job.core.vacancy.command.GetVacancyByIdQuery
import com.job.core.vacancy.dao.VacancyDao
import com.job.core.vacancy.domain.Vacancy

class GetVacancyByIdQueryHandler(
    private val vacancyDao: VacancyDao,
) : CommandHandler<GetVacancyByIdQuery, Vacancy> {

    override suspend fun handle(command: GetVacancyByIdQuery): Vacancy {
        return vacancyDao.findById(command.id) ?: error("Vacancy with id ${command.id} not found")
    }
}