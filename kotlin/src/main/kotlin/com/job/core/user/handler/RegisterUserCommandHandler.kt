package com.job.core.user.handler

import com.job.core.user.command.RegisterUserCommand
import com.job.core.user.dao.UserDao
import com.job.core.user.domain.AccessToken
import com.job.core.user.domain.UserRole
import com.job.core.user.domain.UserStatus
import com.job.core.user.domain.command.CreateUserDomainCommand
import com.job.core.user.service.TokenManager
import com.job.library.command.CommandHandler
import com.job.library.common.uuid.Uuid
import com.job.library.security.PasswordEncoder
import java.time.Instant

class RegisterUserCommandHandler(
    private val userDao: UserDao,
    private val passwordEncoder: PasswordEncoder,
    private val tokenManager: TokenManager,
) : CommandHandler<RegisterUserCommand, AccessToken> {

    override suspend fun handle(command: RegisterUserCommand): AccessToken {
        validateCommand(command)

        val userId = Uuid.v7()

        val domainCommand = CreateUserDomainCommand(
            id = userId,
            firstName = command.firstName,
            lastName = command.lastName,
            passwordHash = passwordEncoder.encode(command.password),
            registeredAt = Instant.now(),
            email = command.email,
            emailVerified = false,
            role = command.role,
            userStatus = UserStatus.ACTIVE,
        )

        userDao.create(domainCommand)

        return tokenManager.generateAccessToken(
            userId = userId,
            email = command.email,
            userRole = command.role,
        )
    }

    private fun validateCommand(command: RegisterUserCommand) {
        if (command.role == UserRole.ADMIN) {
            error("Cannot register user with role ${UserRole.ADMIN}")
        }
    }
}