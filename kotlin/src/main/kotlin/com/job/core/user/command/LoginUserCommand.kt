package com.job.core.user.command

import com.job.core.user.domain.AccessToken
import com.job.library.command.AbstractCommand
import com.job.library.command.UriAware

data class LoginUserCommand(
    val email: String,
    val password: String,
) : AbstractCommand<AccessToken>() {
    companion object : UriAware {
        override fun uri(): String = "com.job.user.login"
    }
}