package com.job.core.vacancy.command

import com.job.core.user.domain.UserRole
import com.job.core.vacancy.domain.ExperienceLevel
import com.job.core.vacancy.domain.WorkType
import com.job.library.command.UnitCommand
import com.job.library.command.UriAware
import com.job.library.common.money.Money
import com.job.library.security.WithRoles
import java.util.EnumSet

data class CreateVacancyCommand(
    val title: String,
    val salaryMin: Money?,
    val salaryMax: Money?,
    val workType: WorkType?,
    val location: String?,
    val description: String?,
    val experienceLevel: ExperienceLevel?,
    val createdAtMillis: Long,
    val editedAtMillis: Long,
) : UnitCommand {

    companion object : UriAware, WithRoles {
        override fun uri(): String = "com.job.vacancy.create"

        override fun roles(): Set<UserRole> = EnumSet.of(UserRole.EMPLOYER)
    }
}
