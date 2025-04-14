package com.job.library.http.middleware

class MiddlewareRegistry(
    private val middlewares: List<Middleware>
) {

    fun getMiddlewares(): List<Middleware> = middlewares
}