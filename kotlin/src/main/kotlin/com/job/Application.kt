package com.job

import com.job.library.cors.configureCORS
import com.job.library.flyway.configureFlyway
import com.job.library.http.configurePostRpcEndpoints
import io.ktor.server.application.*
import io.ktor.server.engine.*
import io.ktor.server.netty.*

fun main() {
    embeddedServer(Netty, port = 8080, host = "0.0.0.0", module = Application::module)
        .start(wait = true)
}

fun Application.module() {
    configurePostRpcEndpoints()
    configureFlyway()
    configureCORS()
}