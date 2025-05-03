package com.job.library.http.middleware

import com.job.core.user.domain.UserRole
import com.job.core.user.service.TokenManager
import com.job.library.command.Command
import com.job.library.command.SubjectRegistry
import com.job.library.command.UriAware
import com.job.library.common.security.Subject
import com.job.library.http.middleware.exception.InsufficientPermissionsException
import com.job.library.security.WithRoles
import io.ktor.http.*
import java.util.UUID
import javax.lang.model.element.NestingKind.ANONYMOUS
import kotlin.reflect.full.companionObjectInstance

const val AUTHORIZATION_HEADER = "Authorization"

class AuthenticationMiddleware(
    private val tokenManager: TokenManager,
    private val subjectRegistry: SubjectRegistry,
) : Middleware {

    companion object {
        private const val BEARER_PREFIX = "Bearer "
    }

    override fun doBefore(command: Command<*>, headers: Headers) {
        val companion = command::class.companionObjectInstance ?: return

        if (companion is WithRoles && companion is UriAware) {
            val roles = companion.roles()

            val subject = if (headers.contains(AUTHORIZATION_HEADER)) {
                val token = headers[AUTHORIZATION_HEADER]?.removePrefix(BEARER_PREFIX) ?: error("must not be empty")

                tokenManager.decodeToken(token)
            } else {
                Subject(
                    userId = UUID.fromString("ac740f33-5c7c-4059-a77c-ee67d3b1a3f3"),
                    email = "fake@gmail.com",
                    role = UserRole.ANONYMOUS
                )
            }

            subjectRegistry.registerSubject(command.uniqueCommandId, subject)

            if (subject.role == UserRole.ADMIN) return // admin can do everything

            if (subject.role !in roles) {
                throw InsufficientPermissionsException(companion.uri())
            }
        }
    }

    override fun doAfter(command: Command<*>, headers: Headers) {
        subjectRegistry.removeSubject(commandId = command.uniqueCommandId)
    }
}