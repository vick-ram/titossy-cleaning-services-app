package com.example.titossycleaningservicesapp.presentation.auth.utils

fun passwordValidation(password: String): Boolean {
   /* val errors = listOf(
        "Password must have at least eight characters!",
        "Password must not contain whitespace!",
        "Password must contain at least one digit!",
        "Password must have at least one uppercase letter!",
        "Password must have at least one special character, such as: _%-=+#@"
    )*/

    val conditions = listOf(
        password.length >= 8,
        password.none { it.isWhitespace() },
        password.any { it.isDigit() },
        password.any { it.isUpperCase() },
        password.any { !it.isLetterOrDigit() }
    )

    return conditions.all { it }
}