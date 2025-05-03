package com.job.core.company.domain.command

import java.util.UUID

data class EditCompanyDomainCommand(
    val id: UUID,
    val name: String,
    val employeesCount: Int?,
    val description: String?,
    val address: String?,
    val websiteLink: String?,
)
