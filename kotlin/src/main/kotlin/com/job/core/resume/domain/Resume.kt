package com.job.core.resume.domain

import java.util.UUID

data class Resume(
    val id: UUID,
    val userId: UUID,
    val title: String,
    val summary: String? = null,
    val createdAtMillis: Long? = null,
    val editedAtMillis: Long? = null,
    val isActive: Boolean = true,
    val experience: List<ExperienceItem>? = null
)