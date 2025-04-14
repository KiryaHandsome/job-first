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


fun Application.configurePostRpcEndpoints() {
    val router = di.direct.instance<RpcRouter>()

    routing {
        post("/api/{uri}") {
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
        }
    }
}

private fun toResponse(contentType: ContentType, body: Any): Any {
    return when (contentType) {
        ContentType.Application.Json -> toJson(body)
        else -> error("Unsupported Content-Type: $contentType")
    }
}

private fun toJson(body: Any): Any {
    val objectMapper = di.direct.instance<ObjectMapper>()

    return objectMapper.writeValueAsString(body)
}