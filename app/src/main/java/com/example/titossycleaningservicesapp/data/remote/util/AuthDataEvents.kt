package com.example.titossycleaningservicesapp.data.remote.util

sealed class AuthEvent {
    data object Loading : AuthEvent()
    data class Success(val message: String? = null) : AuthEvent()
    data class Error(val message: String) : AuthEvent()
}

