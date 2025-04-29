package com.job.core.vacancy.command

import com.job.core.user.domain.UserRole
import com.job.core.vacancy.domain.Vacancy
import com.job.core.vacancy.dto.GetVacanciesFilters
import com.job.core.vacancy.dto.VacancyDto
import com.job.library.command.AbstractCommand
import com.job.library.command.UriAware
import com.job.library.common.pagination.Cursor
import com.job.library.common.pagination.Page
import com.job.library.common.pagination.WithCursor
import com.job.library.security.WithRoles
import java.util.EnumSet

class GetVacanciesWithCursorQuery(
    val filters: GetVacanciesFilters,
    override val cursor: Cursor,
) : AbstractCommand<Page<VacancyDto>>(), WithCursor {

    companion object : UriAware, WithRoles {
        override fun uri(): String = "com.job.vacancy.get_vacancies_with_cursor"

        override fun roles(): Set<UserRole> = EnumSet.of(UserRole.ANONYMOUS, UserRole.USER, UserRole.EMPLOYER)
    }
}
