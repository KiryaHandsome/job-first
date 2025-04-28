package com.job.library.common.security

import com.job.core.user.domain.UserRole
import java.util.UUID

data class Subject(
    val userId: UUID,
    val email: String,
    val role: UserRole,
)
