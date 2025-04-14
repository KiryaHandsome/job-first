package com.job.core.user.handler

import com.job.core.user.command.LoginUserCommand
import com.job.core.user.dao.UserDao
import com.job.core.user.domain.AccessToken
import com.job.core.user.service.TokenManager
import com.job.library.command.CommandHandler
import com.job.library.security.PasswordEncoder

class LoginUserCommandHandler(
    private val userDao: UserDao,
    private val passwordEncoder: PasswordEncoder,
    private val tokenManager: TokenManager,
) : CommandHandler<LoginUserCommand, AccessToken> {

    companion object {
        val invalidCredentialsError = IllegalStateException("User with such credentials not found")
    }

    override suspend fun handle(command: LoginUserCommand): AccessToken {
        val creds = userDao.findCredentialsByEmail(command.email) ?: throw invalidCredentialsError

        if (!passwordEncoder.verifyPassword(command.password, creds.passwordHash)) {
            throw invalidCredentialsError
        }

        return tokenManager.generateAccessToken(email = creds.email, userRole = creds.role)
    }
}