package com.example.titossycleaningservicesapp.presentation.auth.utils

sealed class AuthUiState {
    data object Loading : AuthUiState()
    data object Authenticated : AuthUiState()
    data class Unauthenticated(val message: String? = null) : AuthUiState()
    data class ValidationErrors(val message: Map<String, List<String>>) : AuthUiState()
    data class Error(val message: String) : AuthUiState()
}