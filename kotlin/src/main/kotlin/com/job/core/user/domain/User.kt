package com.job.core.user.domain

import java.time.Instant
import java.time.LocalDate
import java.util.UUID

data class User(
    val id: UUID,
    val email: String,
    val passwordHash: String,
    val emailVerified: Boolean,
    val role: UserRole,
    val registeredAt: Instant,
    val status: UserStatus?,
    val firstName: String?,
    val lastName: String?,
    val middleName: String?,
    val birthDate: LocalDate?,
)
