package com.job.library.jooq.exception

class IntegrityViolationException(override val cause: Throwable?) : RuntimeException(cause)