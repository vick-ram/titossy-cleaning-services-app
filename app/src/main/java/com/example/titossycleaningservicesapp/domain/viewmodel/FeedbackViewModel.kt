package com.example.titossycleaningservicesapp.domain.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.titossycleaningservicesapp.core.Resource
import com.example.titossycleaningservicesapp.domain.models.ui_models.FeedbackUiState
import com.example.titossycleaningservicesapp.domain.repository.FeedbackRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class FeedbackViewModel @Inject constructor(
    private val feedbackRepository: FeedbackRepository
) : ViewModel() {

    private val _feedbackState = MutableStateFlow(FeedbackUiState(isLoading = true))
    val feedbackState = _feedbackState.asStateFlow()


    init {
        getFeedbacks()
    }

    fun giveFeedback(
        bookingId: String,
        feedback: String,
        rating: Double
    ) = viewModelScope.launch {
        feedbackRepository.sendFeedback(
            bookingId = bookingId,
            message = feedback,
            rating = rating
        ).collectLatest { resource ->
            when(resource) {
                is Resource.Error -> {
                    _feedbackState.update {
                        it.copy(
                            isLoading = false,
                            errorMessage = resource.message.toString()
                        )
                    }
                }
                is Resource.Loading -> {
                    _feedbackState.update {
                        it.copy(
                            isLoading = true
                        )
                    }
                }
                is Resource.Success -> {
                    _feedbackState.update {
                        it.copy(
                            isLoading = false,
                            successMessage = resource.data.toString()
                        )
                    }
                }
            }
        }
    }

    private fun getFeedbacks() = viewModelScope.launch {
        feedbackRepository.getFeedbacks().collect { resource ->
            when(resource) {
                is Resource.Error -> {
                    _feedbackState.update {
                        it.copy(
                            isLoading = false,
                            errorMessage = resource.message.toString()
                        )
                    }
                }
                is Resource.Loading -> {
                    _feedbackState.update {
                        it.copy(
                            isLoading = true
                        )
                    }
                }
                is Resource.Success -> {
                    _feedbackState.update {
                        it.copy(
                            isLoading = false,
                            feedbacks = resource.data ?: emptyList()
                        )
                    }
                }
            }
        }
    }
}