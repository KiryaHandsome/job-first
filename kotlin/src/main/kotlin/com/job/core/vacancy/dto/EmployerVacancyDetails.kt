package com.job.core.vacancy.dto

import com.job.core.vacancy.domain.ExperienceLevel
import com.job.core.vacancy.domain.VacancyStatus
import com.job.core.vacancy.domain.WorkType
import com.job.library.common.money.Money
import java.util.UUID

data class EmployerVacancyDetails(
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
    val createdAtMillis: Long,
    val editedAtMillis: Long,
    val applies: List<ApplyResumeDto>
)
