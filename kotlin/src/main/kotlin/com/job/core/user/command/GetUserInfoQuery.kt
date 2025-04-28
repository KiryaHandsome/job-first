package com.job.core.user.command

import com.job.core.user.domain.User
import com.job.core.user.domain.UserRole
import com.job.library.command.AbstractCommand
import com.job.library.command.UriAware
import com.job.library.security.WithRoles
import java.util.EnumSet

class GetUserInfoQuery : AbstractCommand<User>() {
    companion object : UriAware, WithRoles {
        override fun uri(): String = "com.job.user.query.get_user_info"

        override fun roles(): Set<UserRole> = EnumSet.of(UserRole.USER)
    }
}
