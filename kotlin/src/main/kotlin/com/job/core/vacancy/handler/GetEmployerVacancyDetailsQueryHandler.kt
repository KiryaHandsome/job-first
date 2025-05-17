package com.job.core.vacancy.handler

import com.job.core.vacancy.command.GetEmployerVacancyDetailsQuery
import com.job.core.vacancy.dao.VacancyDao
import com.job.core.vacancy.dto.EmployerVacancyDetails
import com.job.library.command.CommandHandler

class GetEmployerVacancyDetailsQueryHandler(
    private val vacancyDao: VacancyDao,
) : CommandHandler<GetEmployerVacancyDetailsQuery, EmployerVacancyDetails> {

    override suspend fun handle(query: GetEmployerVacancyDetailsQuery): EmployerVacancyDetails {
        val vacancy = vacancyDao.findById(query.vacancyId) ?: error("Vacancy not found")

        val applies = vacancyDao.getVacancyApplies(vacancyId = query.vacancyId)

        return EmployerVacancyDetails(
            id = vacancy.id,
            status = vacancy.status,
            publisher = vacancy.publisher,
            title = vacancy.title,
            salaryMin = vacancy.salaryMin,
            salaryMax = vacancy.salaryMax,
            workType = vacancy.workType,
            location = vacancy.location,
            description = vacancy.description,
            experienceLevel = vacancy.experienceLevel,
            createdAtMillis = vacancy.createdAtMillis,
            editedAtMillis = vacancy.editedAtMillis,
            applies = applies.sortedByDescending { it.appliedAt },
        )
    }
}