package com.job.core.resume.domain

import java.time.LocalDate

data class ExperienceItem(
    val startDate: LocalDate,
    val endDate: LocalDate?,
    val company: String,
    val position: String,
    val description: String,
)
