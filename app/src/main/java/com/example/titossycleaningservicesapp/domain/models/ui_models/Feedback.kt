package com.example.titossycleaningservicesapp.domain.models.ui_models

data class Feedback(
    val feedbackId: String,
    val customerId: String?,
    val bookingId: String,
    val feedback: String,
    val rating: Double,
    val createdAt: String,
    val updatedAt: String,
)

data class FeedbackUiState(
    val isLoading: Boolean = false,
    val successMessage: String = "",
    val errorMessage: String = "",
    val feedbacks: List<Feedback> = emptyList()
)
