package com.example.titossycleaningservicesapp.presentation.auth.utils

import com.google.gson.Gson

fun decodeToken(token: String): Map<*, *>? {
    val payload = token.split("\\.".toRegex())[1]
    val decodedPayload = String(java.util.Base64.getUrlDecoder().decode(payload))

    return try {
        Gson().fromJson(decodedPayload, Map::class.java) as Map<*, *>
    } catch (e: Exception) {
        null
    }
}