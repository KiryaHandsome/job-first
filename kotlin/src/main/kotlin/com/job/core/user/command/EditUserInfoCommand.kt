package com.job.core.user.command

import com.job.core.user.domain.UserRole
import com.job.library.command.UnitCommand
import com.job.library.command.UriAware
import com.job.library.security.WithRoles
import java.time.LocalDate
import java.util.EnumSet

data class EditUserInfoCommand(
    val email: String,
    val firstName: String?,
    val lastName: String?,
    val middleName: String?,
    val birthDate: LocalDate?,
) : UnitCommand() {
    companion object : UriAware, WithRoles {
        override fun uri(): String = "com.job.user.edit"

        override fun roles(): Set<UserRole> = EnumSet.of(UserRole.USER, UserRole.EMPLOYER)
    }
}
