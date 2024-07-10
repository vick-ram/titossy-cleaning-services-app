package com.example.titossycleaningservicesapp.data.remote.dto

import com.example.titossycleaningservicesapp.domain.models.ui_models.Feedback
import java.util.UUID

data class FeedbackDto(
    val feedbackId: String,
    val customerId: String?,
    val bookingId: String,
    val feedback: String,
    val rating: Double,
    val createdAt: String,
    val updatedAt: String
) {
    fun toFeedback() = Feedback(
        feedbackId = UUID.fromString(feedbackId),
        customerId = UUID.fromString(customerId),
        bookingId = bookingId,
        feedback = feedback,
        rating = rating
    )
}


