package com.job.core.resume.dto

import com.job.core.resume.domain.ExperienceItem
import java.util.UUID

data class ResumeDto(
    val id: UUID,
    val userId: UUID,
    val title: String,
    val summary: String? = null,
    val createdAtMillis: Long? = null,
    val editedAtMillis: Long? = null,
    val isActive: Boolean = true,
    val experience: List<ExperienceItem>? = null
)
