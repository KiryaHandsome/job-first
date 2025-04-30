package com.job.core.vacancy.handler

import com.job.core.user.domain.UserRole
import com.job.core.vacancy.command.GetVacanciesWithCursorQuery
import com.job.core.vacancy.dao.VacancyDao
import com.job.core.vacancy.domain.Vacancy
import com.job.core.vacancy.dto.VacancyDto
import com.job.library.command.CommandHandler
import com.job.library.command.SubjectRegistry
import com.job.library.common.pagination.Page
import java.util.UUID

class GetVacanciesWithCursorQueryHandler(
    private val vacancyDao: VacancyDao,
    private val subjectRegistry: SubjectRegistry,
) : CommandHandler<GetVacanciesWithCursorQuery, Page<VacancyDto>> {

    override suspend fun handle(command: GetVacanciesWithCursorQuery): Page<VacancyDto> {
        val subject = subjectRegistry.getSubject(commandId = command.uniqueCommandId)

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

    private fun Vacancy.toDto(userAppliedVacancies: Set<UUID>): VacancyDto = VacancyDto(
        id = id,
        publisher = publisher,
        title = title,
        salaryMin = salaryMin,
        salaryMax = salaryMax,
        workType = workType,
        location = location,
        description = description,
        experienceLevel = experienceLevel,
        viewsCount = viewsCount,
        createdAtMillis = createdAtMillis,
        editedAtMillis = editedAtMillis,
        userApplied = userAppliedVacancies.contains(id),
    )

}

