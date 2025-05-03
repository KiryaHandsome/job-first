package com.job.core.vacancy.command

import com.job.core.user.domain.UserRole
import com.job.core.vacancy.dto.VacancyDto
import com.job.library.command.AbstractCommand
import com.job.library.command.UriAware
import com.job.library.security.WithRoles
import java.util.EnumSet
import java.util.UUID
import javax.lang.model.element.NestingKind.ANONYMOUS

data class GetVacancyByIdQuery(
    val id: UUID,
) : AbstractCommand<VacancyDto>() {

    companion object : UriAware, WithRoles {
        override fun uri(): String = "com.job.first.vacancy.get_vacancy_by_id"

        override fun roles(): Set<UserRole> = EnumSet.of(
            UserRole.ANONYMOUS,
            UserRole.USER,
            UserRole.EMPLOYER
        )
    }
}
