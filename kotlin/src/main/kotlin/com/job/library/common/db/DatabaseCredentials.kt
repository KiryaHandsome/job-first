package com.job.library.common.db

data class DatabaseCredentials(
    val userName: String,
    val password: String,
    val databaseName: String,
    val host: String,
    val port: Int,
    val driver: String,
) {

    fun getUrl(): String = "jdbc:$driver://$host:$port/$databaseName"
}