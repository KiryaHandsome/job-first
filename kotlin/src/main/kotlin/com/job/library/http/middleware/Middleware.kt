package com.job.library.http.middleware

import com.job.library.command.BaseCommand
import io.ktor.http.Headers

interface Middleware {

    fun process(command: BaseCommand<*>, headers: Headers)
}