package com.job.library.security

import com.job.core.user.domain.UserRole

interface WithRoles {

    fun roles(): Set<UserRole>
}