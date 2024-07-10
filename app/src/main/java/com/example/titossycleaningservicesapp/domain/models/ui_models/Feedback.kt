package com.example.titossycleaningservicesapp.domain.models.ui_models

import java.util.UUID

data class Feedback(
    val feedbackId: UUID,
    val customerId: UUID?,
    val bookingId: String,
    val feedback: String,
    val rating: Double
)

data class FeedbackUiState(
    val isLoading: Boolean = false,
    val successMessage: String = "",
    val errorMessage: String = ""
)
