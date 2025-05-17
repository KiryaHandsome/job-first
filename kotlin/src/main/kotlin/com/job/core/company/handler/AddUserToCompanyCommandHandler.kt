package com.job.core.company.handler

import com.job.core.company.command.AddUserToCompanyCommand
import com.job.core.company.dao.CompanyDao
import com.job.core.user.dao.UserDao
import com.job.core.user.exception.UserNotFoundException
import com.job.library.command.UnitHandler

class AddUserToCompanyCommandHandler(
    private val userDao: UserDao,
    private val companyDao: CompanyDao,
) : UnitHandler<AddUserToCompanyCommand> {
    override suspend fun handle(command: AddUserToCompanyCommand) {
        val user = userDao.findUserByEmail(command.userEmail) ?: throw UserNotFoundException(command.userEmail)

        companyDao.registerUserInCompany(
            userId = user.id,
            companyId = command.companyId,
        )
    }
}