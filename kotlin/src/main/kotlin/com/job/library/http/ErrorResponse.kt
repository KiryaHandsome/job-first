package com.job.library.http

import java.time.Instant

data class ErrorResponse(
    val message: String,
    val timestamp: Instant = Instant.now()
) {
}
