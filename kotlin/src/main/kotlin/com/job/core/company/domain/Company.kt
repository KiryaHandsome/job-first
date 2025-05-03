package com.job.core.company.domain

import java.util.UUID

data class Company(
    val id: UUID,
    val name: String, // unique
    val employeesCount: Int?,
    val description: String?,
    val address: String?,
    val websiteLink: String?,
//    val domain: CompanyDomain?,
)
