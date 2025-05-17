package com.job.core.vacancy.command

import com.job.core.user.domain.UserRole
import com.job.core.vacancy.dto.EmployerVacancyDetails
import com.job.library.command.AbstractCommand
import com.job.library.command.UriAware
import com.job.library.security.WithRoles
import java.util.EnumSet
import java.util.UUID

data class GetEmployerVacancyDetailsQuery(
    val vacancyId: UUID,
) : AbstractCommand<EmployerVacancyDetails>() {

    companion object : UriAware, WithRoles {
        override fun uri(): String = "com.job.vacancy.get_employer_vacancy_details"

        override fun roles(): Set<UserRole> = EnumSet.of(UserRole.EMPLOYER)
    }
}
