package com.job.core.company.handler

import com.job.core.company.command.EditCompanyCommand
import com.job.core.company.dao.CompanyDao
import com.job.core.company.domain.command.EditCompanyDomainCommand
import com.job.library.command.UnitHandler


class EditCompanyCommandHandler(
    private val companyDao: CompanyDao,
) : UnitHandler<EditCompanyCommand> {

    override suspend fun handle(command: EditCompanyCommand) {
        companyDao.edit(
            cmd = EditCompanyDomainCommand(
                id = command.id,
                name = command.name,
                address = command.address,
                description = command.description,
                employeesCount = command.employeesCount,
                websiteLink = command.websiteLink,
            )
        )
    }
}