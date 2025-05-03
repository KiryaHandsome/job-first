package com.job.library.http.middleware.exception

import com.job.library.common.exception.BaseException

class NoHandlerForUriException(uri: String) : BaseException() {

    override val code: String = "http.no_handler_for_uri"

    override val message: String = "No handler for uri: $uri"
}