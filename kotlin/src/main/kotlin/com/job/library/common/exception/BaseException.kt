package com.job.library.common.exception

abstract class BaseException : RuntimeException() {
    abstract val code: String
    abstract override val message: String
}