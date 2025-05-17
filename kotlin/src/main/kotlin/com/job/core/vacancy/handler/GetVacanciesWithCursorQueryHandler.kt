package com.job.core.vacancy.handler

import com.job.core.user.domain.UserRole
import com.job.core.vacancy.command.GetVacanciesWithCursorQuery
import com.job.core.vacancy.dao.VacancyDao
import com.job.core.vacancy.dto.VacancyDto
import com.job.library.command.CommandHandler
import com.job.library.command.SubjectRegistry
import com.job.library.common.pagination.Page

class GetVacanciesWithCursorQueryHandler(
    private val vacancyDao: VacancyDao,
    private val subjectRegistry: SubjectRegistry,
) : CommandHandler<GetVacanciesWithCursorQuery, Page<VacancyDto>> {

    override suspend fun handle(command: GetVacanciesWithCursorQuery): Page<VacancyDto> {
        val subject = subjectRegistry.getSubject(command)

        val userAppliedVacancies = if (subject.role == UserRole.USER) {
            vacancyDao.getUserApplies(userId = subject.userId)
        } else {
            emptySet()
        }

        val vacancies = vacancyDao.getPageWithCursorAndFilter(
            cursor = command.cursor,
            filters = command.filters
        )

        return Page(
            data = vacancies.data.map { VacancyDto.fromModel(it, userAppliedVacancies) },
            pageInfo = vacancies.pageInfo,
        )
    }
}

