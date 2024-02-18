package com.example.titossycleaningservicesapp.presentation.auth.utils

fun emailAndPasswordValidation(email: String, password: String): Boolean {
    return emailValidation(email) && passwordValidation(password)
}