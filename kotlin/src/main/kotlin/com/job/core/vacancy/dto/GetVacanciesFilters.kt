package com.job.core.vacancy.dto

import com.job.core.vacancy.domain.ExperienceLevel
import com.job.core.vacancy.domain.WorkType
import com.job.library.common.money.Currency
import java.math.BigInteger

data class GetVacanciesFilters(
    val search: String? = null,
    val salaryFrom: BigInteger? = null,
    val currency: Currency? = null,
    val experience: ExperienceLevel? = null,
    val workType: WorkType? = null,
)