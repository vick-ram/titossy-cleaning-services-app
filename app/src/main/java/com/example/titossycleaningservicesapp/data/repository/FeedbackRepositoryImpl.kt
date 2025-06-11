package com.example.titossycleaningservicesapp.data.repository

import com.example.titossycleaningservicesapp.core.FileUtils
import com.example.titossycleaningservicesapp.core.Resource
import com.example.titossycleaningservicesapp.data.remote.api.ApiService
import com.example.titossycleaningservicesapp.domain.models.requests.booking.FeedBackRequest
import com.example.titossycleaningservicesapp.domain.models.ui_models.Feedback
import com.example.titossycleaningservicesapp.domain.repository.FeedbackRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class FeedbackRepositoryImpl @Inject constructor(
    private val apiService: ApiService
) : FeedbackRepository{
    override fun sendFeedback(
        bookingId: String,
        message: String,
        rating: Double
    ): Flow<Resource<String>> {
        return flow {
            emit(Resource.Loading)
            val response = apiService.sendFeedback(
                FeedBackRequest(
                    bookingId = bookingId,
                    feedback = message,
                    rating = rating
                )
            )

            when(response.status) {
                "success" -> {
                    val successMessage = response.message
                    successMessage?.let { emit(Resource.Success(it)) }
                }
                "error" -> {
                    if (response.error != null) {
                        val errors = FileUtils.createErrorMessage(response.error)
                        throw Exception(errors)
                    }
                }
            }
        }.catch { e ->
            e.printStackTrace()
            emit(Resource.Error(e.message.toString()))
        }
    }

    override fun getFeedbacks(): Flow<Resource<List<Feedback>>> {
        return flow {
            emit(Resource.Loading)
            val response = apiService.getFeedbacks()

            when(response.status) {
                "success" -> {
                    val feedbacks = response.data?.map { it.toFeedback() } ?: emptyList()
                    emit(Resource.Success(feedbacks))
                }
                "error" -> {
                    if (response.error != null) {
                        val errors = FileUtils.createErrorMessage(response.error)
                        throw Exception(errors)
                    }
                }
            }
        }.catch { e ->
            e.printStackTrace()
            emit(Resource.Error(e.message.toString()))
        }
    }
}