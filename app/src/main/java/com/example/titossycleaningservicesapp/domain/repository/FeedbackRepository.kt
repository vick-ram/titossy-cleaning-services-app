package com.example.titossycleaningservicesapp.domain.repository

import com.example.titossycleaningservicesapp.core.Resource
import com.example.titossycleaningservicesapp.domain.models.ui_models.Feedback
import kotlinx.coroutines.flow.Flow

interface FeedbackRepository {
    fun sendFeedback(
        bookingId: String,
        message: String,
        rating: Double
    ) : Flow<Resource<String>>
    fun getFeedbacks(): Flow<Resource<List<Feedback>>>
}