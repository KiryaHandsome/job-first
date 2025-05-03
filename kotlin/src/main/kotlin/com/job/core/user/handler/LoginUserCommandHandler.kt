package com.job.core.user.handler

import com.job.core.user.command.LoginUserCommand
import com.job.core.user.dao.UserDao
import com.job.core.user.domain.AccessToken
import com.job.core.user.service.TokenManager
import com.job.library.command.CommandHandler
import com.job.library.security.PasswordEncoder

class LoginUserCommandHandler(
    private val userDao: UserDao,
    private val tokenManager: TokenManager,
    private val passwordEncoder: PasswordEncoder,
) : CommandHandler<LoginUserCommand, AccessToken> {

    companion object {
        val invalidCredentialsError = IllegalStateException("User with such credentials not found")
    }

    override suspend fun handle(command: LoginUserCommand): AccessToken {
        val userLoginInfo = userDao.findUserLoginInfoByEmail(command.email) ?: throw invalidCredentialsError

        if (!passwordEncoder.verifyPassword(command.password, userLoginInfo.passwordHash)) {
            throw invalidCredentialsError
        }

        return tokenManager.generateAccessToken(
            email = userLoginInfo.email,
            userRole = userLoginInfo.role,
            userId = userLoginInfo.userId,
        )
    }
}