package com.job.core.resume.domain.command

import com.job.core.resume.domain.ExperienceItem
import java.util.UUID

data class UpdateResumeDomainCommand(
    val id: UUID,
    val userId: UUID,
    val title: String,
    val summary: String?,
    val editedAtMillis: Long,
    val isActive: Boolean,
    val experience: List<ExperienceItem>?,
)
