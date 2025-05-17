package com.job.core.vacancy.command

import com.job.core.user.domain.UserRole
import com.job.core.vacancy.domain.ExperienceLevel
import com.job.core.vacancy.domain.WorkType
import com.job.library.command.UnitCommand
import com.job.library.command.UriAware
import com.job.library.common.money.Currency
import com.job.library.security.WithRoles
import java.util.UUID

data class EditVacancyCommand(
    val id: UUID,
    val title: String,
    val description: String,
    val location: String,
    val salaryMin: Long?,
    val salaryMax: Long?,
    val salaryCurrency: Currency,
    val experienceLevel: ExperienceLevel,
    val workType: WorkType,
) : UnitCommand() {

    companion object : UriAware, WithRoles {
        override fun uri(): String = "com.job.vacancy.edit"

        override fun roles(): Set<UserRole> = setOf(UserRole.EMPLOYER)
    }
}
