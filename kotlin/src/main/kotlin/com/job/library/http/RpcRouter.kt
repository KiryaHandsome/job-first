package com.job.library.http

import com.fasterxml.jackson.databind.ObjectMapper
import com.job.library.command.BaseCommand
import com.job.library.command.CommandHandlerRegistry
import com.job.library.common.pagination.WithCursor
import com.job.library.http.middleware.MiddlewareRegistry
import com.job.library.http.response.ErrorResponse
import com.job.library.http.response.HttpResponse
import io.ktor.http.*
import org.slf4j.LoggerFactory
import java.io.InputStream

class RpcRouter(
    private val objectMapper: ObjectMapper,
    private val commandHandlerRegistry: CommandHandlerRegistry,
    private val middlewareRegistry: MiddlewareRegistry,
) {

    companion object {
        private val logger = LoggerFactory.getLogger(RpcRouter::class.java)
    }

    suspend fun routePost(uri: String, body: InputStream, headers: Headers): HttpResponse {
        try {
            val handlerInfo = commandHandlerRegistry.findHandlerInfo(uri) ?: error("No handler found for uri $uri")

            val command = objectMapper.readValue(body, handlerInfo.commandClass.java)

            validateCommand(command)

            runMiddlewares(command, headers)

            val response = handlerInfo.commandHandler.handle(command)

            return HttpResponse(statusCode = HttpStatusCode.OK, response)
        } catch (e: Throwable) {
            logger.error("Error occurred on uri {}. Message: {}", uri, e.message, e)

            return HttpResponse(
                statusCode = HttpStatusCode.InternalServerError,
                body = ErrorResponse(
                    message = e.message ?: "Internal Server Error",
                )
            )
        }
    }

    private fun runMiddlewares(command: BaseCommand<*>, headers: Headers) {
        middlewareRegistry.getMiddlewares()
            .forEach { it.process(command, headers) }
    }

    private fun validateCommand(command: BaseCommand<*>) {
        if (command is WithCursor) {
            command.cursor.validate()
        }
    }
}