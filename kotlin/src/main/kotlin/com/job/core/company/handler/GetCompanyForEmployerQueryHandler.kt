package com.job.core.company.handler

import com.job.core.company.command.GetCompanyForEmployerQuery
import com.job.core.company.dao.CompanyDao
import com.job.core.company.domain.Company
import com.job.core.company.exception.UserNotRegisteredInCompanyException
import com.job.library.command.CommandHandler
import com.job.library.command.SubjectRegistry

class GetCompanyForEmployerQueryHandler(
    private val companyDao: CompanyDao,
    private val subjectRegistry: SubjectRegistry,
) : CommandHandler<GetCompanyForEmployerQuery, Company> {

    override suspend fun handle(command: GetCompanyForEmployerQuery): Company {
        val subject = subjectRegistry.getSubject(commandId = command.uniqueCommandId)

        return companyDao.findCompanyByEmployerId(employerId = subject.userId)
            ?: throw UserNotRegisteredInCompanyException()
    }
}