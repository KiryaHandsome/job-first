package com.job.library.http

import com.fasterxml.jackson.databind.ObjectMapper
import com.job.core.di.di
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import org.kodein.di.direct
import org.kodein.di.instance
import org.slf4j.LoggerFactory


fun Application.configurePostRpcEndpoints() {
    val router = di.direct.instance<RpcRouter>()

    val logger = LoggerFactory.getLogger(Application::class.java)

    routing {
        post("/api/{uri}") {
            try {
                val uri = requireNotNull(call.parameters["uri"]) { "Uri must be specified" }

                val response = router.routePost(
                    uri = uri,
                    body = call.receive(),
                    headers = call.request.headers
                )

                call.response.header(
                    HttpHeaders.ContentType,
                    response.responseType.toString()
                )

                val responseBody = toResponse(contentType = response.responseType, response.body ?: "")

                call.respond(status = response.statusCode, message = responseBody)
            } catch (e: Throwable) {
                logger.error("Error during request. Message: {}", e.message, e)

                call.respond(
                    status = HttpStatusCode.InternalServerError, message = """
                        { 
                            "error" : "${e.message}"
                        }
                    """.trimIndent()
                )
            }
        }
    }
}

private fun toResponse(contentType: ContentType, body: Any): String {
    return when (contentType) {
        ContentType.Application.Json -> toJson(body)
        else -> error("Unsupported Content-Type: $contentType")
    }
}

private fun toJson(body: Any): String {
    val objectMapper = di.direct.instance<ObjectMapper>()

    return objectMapper.writeValueAsString(body)
}