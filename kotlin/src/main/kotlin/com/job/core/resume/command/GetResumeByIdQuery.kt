package com.job.core.resume.command

import com.job.core.resume.domain.Resume
import com.job.core.user.domain.UserRole
import com.job.library.command.AbstractCommand
import com.job.library.command.UriAware
import com.job.library.security.WithRoles
import java.util.EnumSet
import java.util.UUID

data class GetResumeByIdQuery(
    val resumeId: UUID,
) : AbstractCommand<Resume>() {

    companion object : UriAware, WithRoles {
        override fun uri(): String = "com.job.resume.get_resume_by_id"

        override fun roles(): Set<UserRole> = EnumSet.of(UserRole.USER, UserRole.EMPLOYER)
    }
}
