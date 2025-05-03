package com.job.core.company.command

import com.job.core.user.domain.UserRole
import com.job.library.command.UnitCommand
import com.job.library.command.UriAware
import com.job.library.security.WithRoles
import java.util.EnumSet
import java.util.UUID

data class EditCompanyCommand(
    val id: UUID,
    val name: String,
    val employeesCount: Int?,
    val description: String?,
    val address: String?,
    val websiteLink: String?,
) : UnitCommand() {

    companion object : UriAware, WithRoles {
        override fun uri(): String = "com.job.company.edit"

        override fun roles(): Set<UserRole> = EnumSet.of(UserRole.EMPLOYER)
    }
}
