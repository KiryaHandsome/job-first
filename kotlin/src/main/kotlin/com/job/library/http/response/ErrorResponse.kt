package com.job.library.http.response

import java.time.Instant

data class ErrorResponse(
    val code: String = "runtime",
    val message: String,
    val timestamp: Instant = Instant.now()
)