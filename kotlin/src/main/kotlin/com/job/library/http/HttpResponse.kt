package com.job.library.http

import io.ktor.http.*

data class HttpResponse(
    val statusCode: HttpStatusCode,
    val body: Any?,
    val responseType: ContentType = ContentType.Application.Json,
)
