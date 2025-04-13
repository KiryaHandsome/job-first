package com.job.core.user.di

import com.job.library.di.autoBind
import com.job.core.user.dao.UserDao
import com.job.core.user.handler.RegisterUserCommandHandler
import org.kodein.di.DI

val userModule = DI.Module("userModule") {
    // DAO
    autoBind<UserDao>()


    // handlers
    autoBind<RegisterUserCommandHandler>()

}

