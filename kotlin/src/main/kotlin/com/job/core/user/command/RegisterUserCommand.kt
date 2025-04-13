package com.job.core.user.command

import com.job.library.command.BaseCommand
import com.job.library.command.UriAware
import com.job.core.user.domain.UserRole

data class RegisterUserCommand(
    val email: String,
    val role: UserRole,
    val password: String,
) : BaseCommand<Unit> {

    companion object : UriAware {
        override fun uri(): String = "com.job.user.register"
    }
}
