package com.job.core.user.dao

import com.job.core.user.domain.User
import com.job.core.user.domain.UserCredentials
import com.job.core.user.domain.UserRole
import com.job.core.user.domain.UserStatus
import com.job.core.user.domain.command.CreateUserDomainCommand
import com.job.generated.jooq.tables.User.USER
import com.job.library.jooq.JooqR2dbcContextFactory
import com.job.library.jooq.mapper.RecordMapper
import java.time.Instant

private val userMapper: RecordMapper<User> = {
    User(
        id = it[USER.ID],
        email = it[USER.EMAIL],
        passwordHash = it[USER.PASSWORD_HASH],
        emailVerified = it[USER.EMAIL_VERIFIED],
        role = UserRole.valueOf(it[USER.ROLE]),
        registeredAt = Instant.ofEpochMilli(it[USER.REGISTERED_AT_MILLIS]),
        status = UserStatus.valueOf(it[USER.STATUS]),
        firstName = it[USER.FIRST_NAME],
        lastName = it[USER.LAST_NAME],
        middleName = it[USER.MIDDLE_NAME],
        birthDate = it[USER.BIRTH_DATE]
    )
}

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
                .set(USER.STATUS, cmd.userStatus.name)
                .set(USER.REGISTERED_AT_MILLIS, cmd.registeredAt.toEpochMilli())
        }
    }

    fun findCredentialsByEmail(email: String): UserCredentials? {
        return jooqR2dbcContextFactory.fetchOneAndAwaitNullable(
            mapper = {
                UserCredentials(
                    email = it.get(USER.EMAIL),
                    passwordHash = it.get(USER.PASSWORD_HASH),
                    role = UserRole.valueOf(it.get(USER.ROLE))
                )
            }
        ) {
            select(USER.EMAIL, USER.PASSWORD_HASH, USER.ROLE)
                .from(USER)
                .where(USER.EMAIL.eq(email))
        }
    }
}