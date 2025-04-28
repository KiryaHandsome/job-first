package com.job.core.resume.command

import com.job.core.resume.domain.ExperienceItem
import com.job.core.user.domain.UserRole
import com.job.library.command.UnitCommand
import com.job.library.command.UriAware
import com.job.library.security.WithRoles
import java.util.EnumSet

data class CreateResumeCommand(
    val title: String,
    val summary: String?,
    val isActive: Boolean,
    val experience: List<ExperienceItem>?,
) : UnitCommand() {

    companion object : UriAware, WithRoles {
        override fun uri(): String = "com.job.resume.create_resume"

        override fun roles(): Set<UserRole> = EnumSet.of(UserRole.USER)
    }
}
