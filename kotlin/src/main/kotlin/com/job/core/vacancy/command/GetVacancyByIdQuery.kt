package com.job.core.vacancy.command

import com.job.library.command.BaseCommand
import com.job.library.command.UriAware
import com.job.core.vacancy.domain.Vacancy
import java.util.UUID

data class GetVacancyByIdQuery(
    val id: UUID,
) : BaseCommand<Vacancy> {

    companion object : UriAware {
        override fun uri(): String = "com.job.first.vacancy.get_vacancy_by_id"
    }
}
