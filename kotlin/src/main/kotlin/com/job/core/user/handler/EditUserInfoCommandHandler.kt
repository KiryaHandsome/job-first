package com.job.core.user.handler

import com.job.core.user.command.EditUserInfoCommand
import com.job.core.user.dao.UserDao
import com.job.core.user.domain.command.EditUserInfoDomainCommand
import com.job.library.command.SubjectRegistry
import com.job.library.command.UnitHandler

class EditUserInfoCommandHandler(
    private val userDao: UserDao,
    private val subjectRegistry: SubjectRegistry,
) : UnitHandler<EditUserInfoCommand> {

    override suspend fun handle(command: EditUserInfoCommand) {
        val userSubject = subjectRegistry.getSubject(commandId = command.uniqueCommandId)

        userDao.edit(
            cmd = EditUserInfoDomainCommand(
                id = userSubject.userId,
                email = command.email,
                firstName = command.firstName,
                lastName = command.lastName,
                middleName = command.middleName,
                birthDate = command.birthDate,
            )
        )
    }
}