package com.job.core.user.domain

data class UserCredentials(
    val email: String,
    val passwordHash: String,
    val role: UserRole,
)
