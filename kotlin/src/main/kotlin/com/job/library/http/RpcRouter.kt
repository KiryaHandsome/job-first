package com.job.library.http

import com.fasterxml.jackson.databind.ObjectMapper
import com.job.library.command.Command
import com.job.library.command.CommandHandlerRegistry
import com.job.library.common.exception.BaseException
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
        val handlerInfo = commandHandlerRegistry.findHandlerInfo(uri) ?: error("No handler found for uri $uri")

        try {
            val command = objectMapper.readValue(body, handlerInfo.commandClass.java)

            validateCommand(command)

            runMiddlewaresBefore(command, headers)

            val response = handlerInfo.commandHandler.handle(command)

            return HttpResponse(statusCode = HttpStatusCode.OK, body = response)
        } catch (e: BaseException) {
            logger.error("Error occurred on uri {}. Message: {}", uri, e.message, e)

            return HttpResponse(
                statusCode = HttpStatusCode.OK,
                body = ErrorResponse(
                    code = e.code,
                    message = e.message,
                )
            )
        } catch (e: Throwable) {
            logger.error("Error occurred on uri {}. Message: {}", uri, e.message, e)

            return HttpResponse(
                statusCode = HttpStatusCode.OK,
                body = ErrorResponse(
                    message = e.message ?: "Internal Server Error",
                )
            )
        } finally {
            // todo: rewrite to chain of responsibility
//            runMiddlewaresAfter(command, headers)
        }
    }

    private fun runMiddlewaresBefore(command: Command<*>, headers: Headers) {
        middlewareRegistry.getMiddlewares()
            .forEach { it.doBefore(command, headers) }
    }

    private fun runMiddlewaresAfter(command: Command<*>, headers: Headers) {
        middlewareRegistry.getMiddlewares()
            .forEach { it.doAfter(command, headers) }
    }

    private fun validateCommand(command: Command<*>) {
        if (command is WithCursor) {
            command.cursor.validate()
        }
    }
}