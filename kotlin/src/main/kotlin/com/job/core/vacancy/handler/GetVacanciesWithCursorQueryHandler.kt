package com.job.core.vacancy.handler

import com.job.core.vacancy.command.GetVacanciesWithCursorQuery
import com.job.core.vacancy.dao.VacancyDao
import com.job.core.vacancy.domain.Vacancy
import com.job.library.command.CommandHandler
import com.job.library.common.pagination.Page

class GetVacanciesWithCursorQueryHandler(
    private val vacancyDao: VacancyDao,
) : CommandHandler<GetVacanciesWithCursorQuery, Page<Vacancy>> {

    override suspend fun handle(command: GetVacanciesWithCursorQuery): Page<Vacancy> {
        return vacancyDao.getPageWithCursor(command.cursor)
    }
}

