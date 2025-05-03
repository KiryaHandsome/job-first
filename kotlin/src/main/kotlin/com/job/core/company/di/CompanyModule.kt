package com.job.core.company.di

import com.job.core.company.dao.CompanyDao
import com.job.core.company.handler.CreateCompanyCommandHandler
import com.job.core.company.handler.EditCompanyCommandHandler
import com.job.core.company.handler.GetCompanyForEmployerQueryHandler
import com.job.library.di.autoBind
import org.kodein.di.DI


val companyModule = DI.Module("companyModule") {
    // DAO
    autoBind<CompanyDao>()

    // handlers
    autoBind<GetCompanyForEmployerQueryHandler>()
    autoBind<CreateCompanyCommandHandler>()
    autoBind<EditCompanyCommandHandler>()
}

