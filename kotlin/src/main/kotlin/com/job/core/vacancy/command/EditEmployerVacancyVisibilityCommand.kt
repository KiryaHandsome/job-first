package com.job.core.vacancy.command

import com.job.core.user.domain.UserRole
import com.job.core.vacancy.domain.VacancyStatus
import com.job.library.command.UnitCommand
import com.job.library.command.UriAware
import com.job.library.security.WithRoles
import java.util.EnumSet
import java.util.UUID

data class EditEmployerVacancyVisibilityCommand(
    val id: UUID,
    val status: VacancyStatus,
) : UnitCommand() {
    companion object : UriAware, WithRoles {
        override fun uri(): String = "com.job.vacancy.edit_visibility"

        override fun roles(): Set<UserRole> = EnumSet.of(UserRole.EMPLOYER)
    }
}
