package com.job.core.user.service

import com.auth0.jwt.JWT
import com.auth0.jwt.algorithms.Algorithm
import com.job.core.user.domain.AccessToken
import com.job.core.user.domain.UserRole
import com.job.library.common.security.JwtInfo
import com.job.library.common.security.Subject
import org.slf4j.LoggerFactory
import java.time.Instant
import java.util.UUID

class TokenManager(
    private val jwtInfo: JwtInfo,
) {

    companion object {
        const val EMAIL_CLAIM = "email"
        const val USER_ID_CLAIM = "userId"
        const val ROLE_CLAIM = "role"

        const val TOKEN_EXPIRES_IN_SECONDS = 604800L // 7 days

        private val logger = LoggerFactory.getLogger(this::class.java)
    }

    private val jwtVerifier = JWT
        .require(Algorithm.HMAC256(jwtInfo.secret))
        .withAudience(jwtInfo.audience)
        .withIssuer(jwtInfo.issuer)
        .build()

    fun generateAccessToken(email: String, userRole: UserRole, userId: UUID): AccessToken {
        val tokenExpiresAt = Instant.now().plusSeconds(TOKEN_EXPIRES_IN_SECONDS)

        val token = JWT.create()
            .withAudience(jwtInfo.audience)
            .withIssuer(jwtInfo.issuer)
            .withClaim(EMAIL_CLAIM, email)
            .withClaim(ROLE_CLAIM, userRole.name)
            .withClaim(USER_ID_CLAIM, userId.toString())
            .withExpiresAt(tokenExpiresAt)
            .sign(Algorithm.HMAC256(jwtInfo.secret))

        return AccessToken(accessToken = token, accessTokenExpiresAt = tokenExpiresAt.toEpochMilli())
    }

    fun decodeToken(token: String): Subject {
        try {
            val decoded = JWT.decode(token)

            jwtVerifier.verify(decoded)

            val email = decoded.getClaim(EMAIL_CLAIM).asString()
            val role = decoded.getClaim(ROLE_CLAIM).asString()
            val userId = decoded.getClaim(USER_ID_CLAIM).asString()

            return Subject(
                userId = UUID.fromString(userId),
                email = email,
                role = UserRole.valueOf(role),
            )
        } catch (e: Throwable) {
            logger.error("Error during jwt decoding: {}", e.message, e)

            error("Authentication error")
        }
    }
}