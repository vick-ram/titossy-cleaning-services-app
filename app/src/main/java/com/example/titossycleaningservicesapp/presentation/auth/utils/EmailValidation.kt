package com.example.titossycleaningservicesapp.presentation.auth.utils

fun emailValidation(email: String): Boolean {
    val pattern = "[a-zA-Z0-9\\+\\.\\_\\%\\-\\+]{1,256}" +
            "\\@" +
            "[a-zA-Z0-9][a-zA-Z0-9\\-]{0,64}" +
            "(" +
            "\\." +
            "[a-zA-Z0-9][a-zA-Z0-9\\-]{0,25}" +
            ")+"
    return email.matches(pattern.toRegex())
}