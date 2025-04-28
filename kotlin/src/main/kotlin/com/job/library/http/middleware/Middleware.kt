package com.job.library.http.middleware

import com.job.library.command.Command
import io.ktor.http.*

interface Middleware {

    fun doBefore(command: Command<*>, headers: Headers)

    fun doAfter(command: Command<*>, headers: Headers)
}