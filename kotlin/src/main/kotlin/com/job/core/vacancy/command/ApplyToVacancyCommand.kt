package com.job.core.vacancy.command

import com.job.core.user.domain.UserRole
import com.job.library.command.UnitCommand
import com.job.library.command.UriAware
import com.job.library.security.WithRoles
import java.util.EnumSet
import java.util.UUID

data class ApplyToVacancyCommand(
    val vacancyId: UUID,
    val resumeId: UUID,
) : UnitCommand() {

    companion object : UriAware, WithRoles {
        override fun uri(): String = "com.job.vacancy.apply"

        override fun roles(): Set<UserRole> = EnumSet.of(UserRole.USER)
    }
}
