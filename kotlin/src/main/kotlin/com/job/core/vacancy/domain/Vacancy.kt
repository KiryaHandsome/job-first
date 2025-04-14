package com.job.core.vacancy.domain

import com.job.library.common.money.Money
import java.util.UUID

data class Vacancy(
    val id: UUID,
    val publisher: UUID,
    val title: String,
    val salaryMin: Money?,
    val salaryMax: Money?,
    val workType: WorkType?,
    val location: String?,
    val description: String?,
    val experienceLevel: ExperienceLevel?,
    val viewsCount: Int?,
    val createdAtMillis: Long,
    val editedAtMillis: Long
)