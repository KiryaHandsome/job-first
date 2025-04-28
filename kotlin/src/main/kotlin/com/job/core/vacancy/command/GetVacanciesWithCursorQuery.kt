package com.job.core.vacancy.command

import com.job.core.vacancy.domain.Vacancy
import com.job.library.command.AbstractCommand
import com.job.library.command.UriAware
import com.job.library.common.pagination.Cursor
import com.job.library.common.pagination.Page
import com.job.library.common.pagination.WithCursor

class GetVacanciesWithCursorQuery(
    override val cursor: Cursor,
) : AbstractCommand<Page<Vacancy>>(), WithCursor {

    companion object : UriAware {
        override fun uri(): String = "com.job.vacancy.get_vacancies_with_cursor"
    }
}
