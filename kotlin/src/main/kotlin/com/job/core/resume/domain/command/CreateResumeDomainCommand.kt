package com.job.core.resume.domain.command

import com.job.core.resume.domain.ExperienceItem
import java.util.UUID

data class CreateResumeDomainCommand(
    val id: UUID,
    val userId: UUID,
    val title: String,
    val summary: String?,
    val createdAtMillis: Long,
    val isActive: Boolean,
    val experience: List<ExperienceItem>?,
)
