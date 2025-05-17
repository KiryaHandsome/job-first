package com.job.core.vacancy.dto

import com.job.core.vacancy.domain.ExperienceLevel
import com.job.core.vacancy.domain.Vacancy
import com.job.core.vacancy.domain.VacancyStatus
import com.job.core.vacancy.domain.WorkType
import com.job.library.common.money.Money
import java.util.UUID

data class VacancyDto(
    val id: UUID,
    val status: VacancyStatus,
    val publisher: UUID,
    val title: String,
    val salaryMin: Money?,
    val salaryMax: Money?,
    val workType: WorkType?,
    val location: String?,
    val description: String?,
    val experienceLevel: ExperienceLevel?,
    val appliesCount: Int,
    val createdAtMillis: Long,
    val editedAtMillis: Long,
    val userApplied: Boolean,
) {
    companion object {
        fun fromModel(
            vacancy: Vacancy,
            userAppliedVacancies: Set<UUID>,
            appliesCount: Int = 0,
        ): VacancyDto =
            with(vacancy) {
                VacancyDto(
                    id = id,
                    publisher = publisher,
                    title = title,
                    salaryMin = salaryMin,
                    salaryMax = salaryMax,
                    workType = workType,
                    location = location,
                    description = description,
                    experienceLevel = experienceLevel,
                    appliesCount = appliesCount,
                    createdAtMillis = createdAtMillis,
                    editedAtMillis = editedAtMillis,
                    userApplied = userAppliedVacancies.contains(id),
                    status = vacancy.status,
                )
            }
    }
}
