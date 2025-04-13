package com.job.core.user.handler

import com.job.library.command.CommandHandler
import com.job.library.common.uuid.Uuid
import com.job.library.security.PasswordEncoder
import com.job.core.user.command.RegisterUserCommand
import com.job.core.user.dao.UserDao
import com.job.core.user.domain.command.CreateUserDomainCommand
import java.time.Instant

class RegisterUserCommandHandler(
    private val userDao: UserDao,
    private val passwordEncoder: PasswordEncoder,
) : CommandHandler<RegisterUserCommand, Unit> {

    override suspend fun handle(command: RegisterUserCommand) {
        val domainCommand = CreateUserDomainCommand(
            id = Uuid.v7(),
            passwordHash = passwordEncoder.encode(command.password),
            registeredAt = Instant.now(),
            email = command.email,
            emailVerified = false,
            role = command.role,
        )

        userDao.create(domainCommand)

        userDao.create(domainCommand)
    }
}