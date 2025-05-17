package com.job.core.vacancy.domain.command

import com.job.core.vacancy.domain.ExperienceLevel
import com.job.core.vacancy.domain.VacancyStatus
import com.job.core.vacancy.domain.WorkType
import com.job.library.common.money.Money
import java.util.UUID

data class CreateVacancyDomainCommand(
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
) {

    init {
        if (salaryMin != null && salaryMax != null) {
            if (salaryMin.currency != salaryMax.currency) {
                error("salaryMin.currency != salaryMax.currency")
            }
        }
    }
}