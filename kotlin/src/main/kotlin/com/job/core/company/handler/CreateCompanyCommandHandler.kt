package com.job.core.company.handler

import com.job.core.company.command.CreateCompanyCommand
import com.job.core.company.dao.CompanyDao
import com.job.core.company.domain.command.CreateCompanyDomainCommand
import com.job.library.command.SubjectRegistry
import com.job.library.command.UnitHandler
import com.job.library.common.uuid.Uuid

class CreateCompanyCommandHandler(
    private val companyDao: CompanyDao,
    private val subjectRegistry: SubjectRegistry,
) : UnitHandler<CreateCompanyCommand> {

    override suspend fun handle(command: CreateCompanyCommand) {
        //todo: check that only employer from company can edit it

        val companyId = Uuid.v7()

        companyDao.create(
            cmd = CreateCompanyDomainCommand(
                id = companyId,
                name = command.name,
                address = command.address,
                description = command.description,
                employeesCount = command.employeesCount,
                websiteLink = command.websiteLink,
            )
        )

        val subject = subjectRegistry.getSubject(commandId = command.uniqueCommandId)

        companyDao.registerUserInCompany(
            userId = subject.userId,
            companyId = companyId,
        )
    }
}