package com.job.core.vacancy.command

import com.job.core.vacancy.domain.Vacancy
import com.job.library.command.AbstractCommand
import com.job.library.command.UriAware
import java.util.UUID

data class GetVacancyByIdQuery(
    val id: UUID,
) : AbstractCommand<Vacancy>() {

    companion object : UriAware {
        override fun uri(): String = "com.job.first.vacancy.get_vacancy_by_id"
    }
}
