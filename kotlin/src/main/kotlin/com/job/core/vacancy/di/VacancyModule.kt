package com.job.core.vacancy.di

import com.job.core.vacancy.dao.VacancyDao
import com.job.core.vacancy.handler.ApplyToVacancyCommandHandler
import com.job.core.vacancy.handler.CreateVacancyCommandHandler
import com.job.core.vacancy.handler.GetVacanciesWithCursorQueryHandler
import com.job.core.vacancy.handler.GetVacancyByIdQueryHandler
import com.job.core.vacancy.handler.UpdateVacancyCommandHandler
import com.job.library.di.autoBind
import org.kodein.di.DI

val vacancyModule = DI.Module("vacancyModule") {
    // DAO
    autoBind<VacancyDao>()


    // handlers
    autoBind<GetVacanciesWithCursorQueryHandler>()
    autoBind<GetVacancyByIdQueryHandler>()
    autoBind<UpdateVacancyCommandHandler>()
    autoBind<CreateVacancyCommandHandler>()
    autoBind<ApplyToVacancyCommandHandler>()

}

