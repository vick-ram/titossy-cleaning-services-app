package com.example.titossycleaningservicesapp.domain.repository

import com.example.titossycleaningservicesapp.core.Resource
import kotlinx.coroutines.flow.Flow

interface FeedbackRepository {
    fun sendFeedback(
        bookingId: String,
        message: String,
        rating: Double
    ) : Flow<Resource<String>>
}