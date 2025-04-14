package com.job.core.user.domain.command

import com.job.core.user.domain.UserRole
import com.job.core.user.domain.UserStatus
import java.time.Instant
import java.util.UUID

data class CreateUserDomainCommand(
    val id: UUID,
    val email: String,
    val emailVerified: Boolean,
    val passwordHash: String,
    val registeredAt: Instant,
    val role: UserRole,
    val userStatus: UserStatus,
)