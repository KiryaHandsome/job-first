package com.job.core.user.dao

import com.job.library.jooq.JooqR2dbcContextFactory
import com.job.generated.jooq.tables.User.USER
import com.job.core.user.domain.User
import com.job.core.user.domain.command.CreateUserDomainCommand

class UserDao(
    private val jooqR2dbcContextFactory: JooqR2dbcContextFactory
) {

    fun create(cmd: CreateUserDomainCommand) {
        jooqR2dbcContextFactory.use {
            insertInto(USER)
                .set(USER.ID, cmd.id)
                .set(USER.EMAIL, cmd.email)
                .set(USER.EMAIL_VERIFIED, cmd.emailVerified)
                .set(USER.PASSWORD_HASH, cmd.passwordHash)
                .set(USER.ROLE, cmd.role.name)
                .set(USER.REGISTERED_AT_MILLIS, cmd.registeredAt.toEpochMilli())
        }
    }

    fun findByPhoneNumber(phoneNumber: String): User? {
        TODO("implement")
    }
}