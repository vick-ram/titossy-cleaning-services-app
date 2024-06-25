package com.example.titossycleaningservicesapp.core

fun splitFullName(fullName: String): Pair<String, String> {
    val parts = fullName.split(" ")
    val firstName = parts.getOrNull(0) ?: ""
    val lastName = if (parts.size > 1) parts.drop(1).joinToString(" ") else ""
    return Pair(firstName, lastName)
}