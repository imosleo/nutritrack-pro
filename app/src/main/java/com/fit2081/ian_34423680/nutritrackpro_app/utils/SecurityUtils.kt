package com.fit2081.ian_34423680.nutritrackpro_app.utils

import java.nio.charset.StandardCharsets
import java.security.MessageDigest

object SecurityUtils {

    fun hashPassword(password: String): String {
        val digest = MessageDigest.getInstance("SHA-256")
        val hash = digest.digest(password.toByteArray(StandardCharsets.UTF_8))
        return hash.joinToString("") { "%02x".format(it) }
    }

    fun verifyPassword(inputPassword: String, storedHash: String): Boolean {
        val inputHash = hashPassword(inputPassword)
        return inputHash == storedHash
    }
}
