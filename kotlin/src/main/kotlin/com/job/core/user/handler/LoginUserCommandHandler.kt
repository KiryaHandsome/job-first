package com.job.core.user.handler

import com.job.core.user.command.LoginUserCommand
import com.job.library.command.CommandHandler

class LoginUserCommandHandler: CommandHandler<LoginUserCommand, AccessToken> {
}