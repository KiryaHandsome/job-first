package com.job.core.vacancy.command

import com.job.library.command.BaseCommand
import com.job.library.command.UriAware
import com.job.library.common.pagination.Cursor
import com.job.library.common.pagination.Page
import com.job.library.common.pagination.WithCursor
import com.job.core.vacancy.domain.Vacancy

class GetVacanciesWithCursorQuery(
    override val cursor: Cursor,
) : BaseCommand<Page<Vacancy>>, WithCursor {

    companion object : UriAware {
        override fun uri(): String = "com.job.vacancy.get_vacancies_with_cursor"
    }
}
