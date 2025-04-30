package com.job.core.vacancy.handler

import com.job.core.user.domain.UserRole
import com.job.core.vacancy.command.GetVacancyByIdQuery
import com.job.core.vacancy.dao.VacancyDao
import com.job.core.vacancy.domain.Vacancy
import com.job.core.vacancy.dto.VacancyDto
import com.job.library.command.CommandHandler
import com.job.library.command.SubjectRegistry

class GetVacancyByIdQueryHandler(
    private val vacancyDao: VacancyDao,
    private val subjectRegistry: SubjectRegistry,
) : CommandHandler<GetVacancyByIdQuery, VacancyDto> {

    override suspend fun handle(command: GetVacancyByIdQuery): VacancyDto {
        val vacancy = vacancyDao.findById(command.id) ?: error("Vacancy with id ${command.id} not found")

        val subject = subjectRegistry.findSubject(command.uniqueCommandId)

        val userAppliedVacancies = if (subject != null && subject.role == UserRole.USER) {
            vacancyDao.getUserApplies(userId = subject.userId)
        } else {
            emptySet()
        }

        return VacancyDto.fromModel(
            vacancy = vacancy,
            userAppliedVacancies = userAppliedVacancies
        )
    }
}