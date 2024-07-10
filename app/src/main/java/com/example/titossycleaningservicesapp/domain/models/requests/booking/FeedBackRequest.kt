package com.example.titossycleaningservicesapp.domain.models.requests.booking

data class FeedBackRequest(
    val bookingId: String,
    val feedback: String,
    val rating: Double
)
