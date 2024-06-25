package com.example.titossycleaningservicesapp.domain.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.titossycleaningservicesapp.core.Resource
import com.example.titossycleaningservicesapp.domain.models.requests.booking.BookingRequest
import com.example.titossycleaningservicesapp.domain.models.requests.booking.UpdateBookingStatus
import com.example.titossycleaningservicesapp.domain.models.ui_models.BookingUiState
import com.example.titossycleaningservicesapp.domain.repository.BookingRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class BookingViewModel @Inject constructor(
    private val bookingRepository: BookingRepository
) : ViewModel() {
    private val _bookingUiState = MutableStateFlow(BookingUiState(isLoading = true))
    val bookingUiState = _bookingUiState.asStateFlow()

    private lateinit var job: Job

    fun fetchBookings() = viewModelScope.launch {
        bookingRepository.getBookings()
            .stateIn(
                scope = viewModelScope,
                started = SharingStarted.WhileSubscribed(5000L),
                initialValue = Resource.Loading
            )
            .map { resource ->
                when (resource) {
                    is Resource.Error -> BookingUiState(
                        isLoading = false,
                        errorMessage = resource.message.toString()
                    )

                    is Resource.Loading -> BookingUiState(
                        isLoading = true
                    )

                    is Resource.Success -> BookingUiState(
                        isLoading = false,
                        bookings = resource.data
                    )
                }
            }
            .collect { state ->
                _bookingUiState.value = state
            }
    }

    fun searchBookings(query: String) {
        job.cancel()
        job = viewModelScope.launch {
            delay(300L)
            bookingRepository.searchBooking(query)
                .map { resource ->
                    when (resource) {
                        is Resource.Error -> BookingUiState(
                            isLoading = false,
                            errorMessage = resource.message.toString()
                        )

                        is Resource.Loading -> BookingUiState(isLoading = true)
                        is Resource.Success -> BookingUiState(
                            isLoading = false,
                            bookings = resource.data
                        )
                    }
                }
                .collectLatest { state ->
                    _bookingUiState.value = state
                }
        }
    }

    fun fetchBookingByID(bookingId: String) = viewModelScope.launch {
        bookingRepository.getBookingById(bookingId)
            .map { resource ->
                when (resource) {
                    is Resource.Error -> BookingUiState(
                        isLoading = false,
                        errorMessage = resource.message.toString()
                    )

                    is Resource.Loading -> BookingUiState(isLoading = true)
                    is Resource.Success -> BookingUiState(
                        isLoading = false,
                        booking = resource.data
                    )
                }
            }
            .collectLatest { state ->
                _bookingUiState.value = state
            }
    }

    fun createBooking(bookingRequest: BookingRequest) = viewModelScope.launch {
        bookingRepository.createBooking(
            bookingDate = bookingRequest.bookingDate,
            bookingTime = bookingRequest.bookingTime,
            frequency = bookingRequest.frequency,
            instructions = bookingRequest.additionalInstructions,
            address = bookingRequest.address
        )
            .collect { resource ->
                when (resource) {
                    is Resource.Error -> {
                        _bookingUiState.update {
                            it.copy(
                                isLoading = false,
                                errorMessage = resource.message.toString()
                            )
                        }
                    }

                    is Resource.Loading -> {
                        _bookingUiState.update {
                            it.copy(
                                isLoading = true
                            )
                        }
                    }

                    is Resource.Success -> {
                        _bookingUiState.update {
                            it.copy(
                                isLoading = false,
                                booking = resource.data
                            )
                        }
                    }
                }
            }
    }

    fun updateBooking(
        bookingId: String,
        bookingRequest: BookingRequest
    ) = viewModelScope.launch {
        bookingRepository.updateBooking(
            bookingId = bookingId,
            bookingDate = bookingRequest.bookingDate,
            bookingTime = bookingRequest.bookingTime,
            frequency = bookingRequest.frequency,
            instructions = bookingRequest.additionalInstructions,
            address = bookingRequest.address
        )
            .map { resource ->
                when (resource) {
                    is Resource.Error -> BookingUiState(
                        isLoading = false,
                        errorMessage = resource.message.toString()
                    )

                    is Resource.Loading -> BookingUiState(isLoading = true)
                    is Resource.Success -> BookingUiState(
                        isLoading = false,
                        isSuccess = resource.data.toString()
                    )
                }
            }
            .collectLatest { state ->
                _bookingUiState.value = state
            }
    }

    fun updateBookingStatus(
        bookingId: String,
        updateBookingStatus: UpdateBookingStatus
    ) = viewModelScope.launch {
        bookingRepository.updateBookingStatus(bookingId, updateBookingStatus.bookingStatus)
            .collect { resource ->
                when (resource) {
                    is Resource.Error -> {
                        _bookingUiState.update {
                            it.copy(
                                isLoading = false,
                                errorMessage = resource.message.toString()
                            )
                        }
                    }

                    is Resource.Loading -> {
                        _bookingUiState.update {
                            it.copy(isLoading = true)
                        }
                    }

                    is Resource.Success -> {
                        _bookingUiState.update {
                            it.copy(
                                isLoading = false,
                                isSuccess = resource.data.toString()
                            )
                        }
                    }
                }
            }
    }

    fun deleteBooking(bookingId: String) = viewModelScope.launch {
        bookingRepository.deleteBooking(bookingId)
            .collect { resource ->
                when (resource) {
                    is Resource.Error -> {
                        _bookingUiState.update {
                            it.copy(
                                isLoading = false,
                                errorMessage = resource.message.toString()
                            )
                        }
                    }

                    is Resource.Loading -> {
                        _bookingUiState.update {
                            it.copy(isLoading = true)
                        }
                    }

                    is Resource.Success -> {
                        _bookingUiState.update {
                            it.copy(
                                isLoading = false,
                                isSuccess = resource.data.toString()
                            )
                        }
                    }
                }
            }
    }

}