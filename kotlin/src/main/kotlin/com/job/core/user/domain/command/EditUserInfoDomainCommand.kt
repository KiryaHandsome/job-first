package com.job.core.user.domain.command

import java.time.LocalDate
import java.util.UUID

data class EditUserInfoDomainCommand(
    val id: UUID,
    val email: String,
    val firstName: String?,
    val lastName: String?,
    val middleName: String?,
    val birthDate: LocalDate?,
)
