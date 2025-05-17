package com.job.core.user.command

import com.job.core.user.domain.AccessToken
import com.job.core.user.domain.UserRole
import com.job.library.command.AbstractCommand
import com.job.library.command.UriAware

data class RegisterUserCommand(
    val email: String,
    val firstName: String,
    val lastName: String,
    val role: UserRole,
    val password: String,
) : AbstractCommand<AccessToken>() {

    companion object : UriAware {
        override fun uri(): String = "com.job.user.register"
    }
}
