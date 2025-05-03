package com.job.core.vacancy.command

import com.job.core.user.domain.UserRole
import com.job.core.vacancy.domain.Vacancy
import com.job.library.command.AbstractCommand
import com.job.library.command.UriAware
import com.job.library.security.WithRoles
import java.util.EnumSet

class GetEmployerVacanciesQuery: AbstractCommand<List<Vacancy>>() {

    companion object : UriAware, WithRoles {
        override fun uri(): String = "com.job.vacancy.get_employer_vacancies"

        override fun roles(): Set<UserRole> = EnumSet.of(UserRole.EMPLOYER)
    }
}