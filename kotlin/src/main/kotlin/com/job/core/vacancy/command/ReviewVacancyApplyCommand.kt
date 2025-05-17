package com.job.core.vacancy.command

import com.job.core.user.domain.UserRole
import com.job.core.vacancy.dto.ReviewVacancyApplyAction
import com.job.library.command.UnitCommand
import com.job.library.command.UriAware
import com.job.library.security.WithRoles
import java.util.UUID


data class ReviewVacancyApplyCommand(
    val id: UUID,
    val action: ReviewVacancyApplyAction,
) : UnitCommand() {

    companion object : UriAware, WithRoles {
        override fun uri(): String = "com.job.vacancy.review_apply"

        override fun roles(): Set<UserRole> = setOf(UserRole.EMPLOYER)
    }
}

