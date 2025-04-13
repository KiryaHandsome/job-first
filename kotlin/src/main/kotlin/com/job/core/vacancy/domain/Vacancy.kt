package com.job.core.vacancy.domain

import java.time.Instant
import java.util.UUID

data class Vacancy(
    val id: UUID,
    val title: String,
    val description: String,
    val company: String,
    val location: String,
    val salary: String?,
    val createdAt: Instant,
    val editedAt: Instant,
)