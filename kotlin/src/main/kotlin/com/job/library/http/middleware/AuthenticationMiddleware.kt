package com.job.library.http.middleware

import com.job.core.user.domain.UserRole
import com.job.core.user.service.TokenManager
import com.job.library.command.Command
import com.job.library.command.SubjectRegistry
import com.job.library.security.WithRoles
import io.ktor.http.*
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

        if (companion is WithRoles) {
            val roles = companion.roles()

            val tokenHeader = headers[AUTHORIZATION_HEADER] ?: error("Authorization header not found")

            val token = tokenHeader.removePrefix(BEARER_PREFIX)

            val subject = tokenManager.decodeToken(token)

            subjectRegistry.registerSubject(command.uniqueCommandId, subject)

            if (subject.role == UserRole.ADMIN) return // admin can do everything

            if (subject.role !in roles) {
                error("Insufficient permissions to perform the operation")
            }
        }
    }

    override fun doAfter(command: Command<*>, headers: Headers) {
        subjectRegistry.removeSubject(commandId = command.uniqueCommandId)
    }
}