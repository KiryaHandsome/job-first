package com.job.core.user.domain

// TODO: think of refresh token
class AccessToken(
    val accessToken: String,
    val accessTokenExpiresAt: Long,
)