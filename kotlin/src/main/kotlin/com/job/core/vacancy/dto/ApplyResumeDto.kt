package com.job.core.vacancy.dto

import com.job.core.vacancy.domain.ApplyStatus
import java.util.UUID

data class ApplyResumeDto(
    val id: UUID,
    val resumeId: UUID,
    val resumeTitle: String,
    val firstName: String,
    val lastName: String,
    val appliedAt: Long?,
    val status: ApplyStatus,
)
