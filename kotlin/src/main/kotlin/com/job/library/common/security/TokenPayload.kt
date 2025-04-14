package com.job.library.common.security

import com.job.core.user.domain.UserRole

data class TokenPayload(
    val email: String,
    val role: UserRole,
)
