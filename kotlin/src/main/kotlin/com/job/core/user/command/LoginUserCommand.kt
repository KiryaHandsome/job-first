package com.job.core.user.command

import com.job.library.command.UriAware

data class LoginUserCommand(
    val email: String,
    val password: String,
) {
    companion object : UriAware {
        override fun uri(): String = "com.job.user.login"
    }
}