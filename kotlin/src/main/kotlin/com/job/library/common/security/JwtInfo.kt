package com.job.library.common.security

data class JwtInfo(
    val issuer: String,
    val audience: String,
    val secret: String,
)
