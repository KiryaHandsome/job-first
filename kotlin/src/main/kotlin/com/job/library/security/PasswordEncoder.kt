package com.job.library.security

import org.mindrot.jbcrypt.BCrypt

class PasswordEncoder(
    private val secretKey: String
) {

    fun encode(password: String): String {
        return BCrypt.hashpw(password, secretKey)
    };

    fun verifyPassword(password: String, passwordHash: String): Boolean {
        return BCrypt.checkpw(password, passwordHash)
    }
}