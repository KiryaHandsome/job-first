package com.job.library.http.middleware

import com.job.core.user.domain.UserRole
import com.job.core.user.service.TokenManager
import com.job.library.command.BaseCommand
import com.job.library.security.WithRoles
import io.ktor.http.*
import kotlin.reflect.full.companionObjectInstance

const val AUTHORIZATION_HEADER = "Authorization"

class AuthenticationMiddleware(
    private val tokenManager: TokenManager,
) : Middleware {

    override fun process(command: BaseCommand<*>, headers: Headers) {
        val companion = command::class.companionObjectInstance ?: return

        if (companion is WithRoles) {
            val roles = companion.roles()

            val token = headers[AUTHORIZATION_HEADER] ?: error("Authorization header not found")

            val tokenPayload = tokenManager.decodeToken(token)

            if (tokenPayload.role == UserRole.ADMIN) return // admin can do everything

            if (tokenPayload.role !in roles) {
                error("Insufficient permissions to perform the operation")
            }
        }
    }
}