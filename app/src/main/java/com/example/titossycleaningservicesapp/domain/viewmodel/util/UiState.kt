package com.example.titossycleaningservicesapp.domain.viewmodel.util

data class UiState(
    val isLoading: Boolean = false,
    val errorMessage: String? = "",
    val isSuccess: String? = ""
)
