package com.job.core.user.handler

import com.job.core.user.command.GetUserInfoQuery
import com.job.core.user.dao.UserDao
import com.job.core.user.domain.User
import com.job.library.command.CommandHandler
import com.job.library.command.SubjectRegistry

class GetUserInfoQueryHandler(
    private val userDao: UserDao,
    private val subjectRegistry: SubjectRegistry,
) : CommandHandler<GetUserInfoQuery, User> {

    override suspend fun handle(command: GetUserInfoQuery): User {
        // TODO: add check that not all users can fetch data of other users
        val userSubject = subjectRegistry.getSubject(command.uniqueCommandId)

        return userDao.findUserByEmail(userSubject.email) ?: error("User with email ${userSubject.email} doesn't exist")
    }
}