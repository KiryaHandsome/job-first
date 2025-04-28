package com.job.core.user.domain

import java.util.UUID

data class UserLoginInfo(
    val userId: UUID,
    val email: String,
    val passwordHash: String,
    val role: UserRole,
)
