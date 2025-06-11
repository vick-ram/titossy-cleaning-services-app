package com.example.titossycleaningservicesapp.domain.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.titossycleaningservicesapp.core.Resource
import com.example.titossycleaningservicesapp.domain.models.requests.booking.BookingRequest
import com.example.titossycleaningservicesapp.domain.models.requests.booking.UpdateBookingStatus
import com.example.titossycleaningservicesapp.domain.models.ui_models.BookingAssignmentUiState
import com.example.titossycleaningservicesapp.domain.models.ui_models.BookingAssignmentUpdateUiState
import com.example.titossycleaningservicesapp.domain.models.ui_models.BookingUiState
import com.example.titossycleaningservicesapp.domain.models.ui_models.BookingUpdateUiState
import com.example.titossycleaningservicesapp.domain.repository.BookingRepository
import dagger.hilt.android.lifecycle.HiltViewModel
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

    private val _bookingUpdateUiState = MutableStateFlow(BookingUpdateUiState())
    val bookingUpdate = _bookingUpdateUiState.asStateFlow()

    private val _bookingAssignmentState =
        MutableStateFlow(BookingAssignmentUiState(isLoading = true))
    val bookingAssignmentUiState = _bookingAssignmentState.asStateFlow()

    private val _bookingAssignmentUpdateUiState =
        MutableStateFlow(BookingAssignmentUpdateUiState(isLoading = true))
    val bookingAssignmentUpdateUiState = _bookingAssignmentUpdateUiState.asStateFlow()

    init {
        fetchBookings()
        fetchCustomerBookings()
    }

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
                        bookings = resource.data ?: emptyList()
                    )
                }
            }
            .collect { state ->
                _bookingUiState.update {
                    it.copy(
                        isLoading = state.isLoading,
                        errorMessage = state.errorMessage,
                        bookings = state.bookings
                    )
                }
            }
        _bookingUiState.update { it.copy(isLoading = false) }
    }

    fun fetchCustomerBookings() = viewModelScope.launch {
        bookingRepository.getCustomerBookings()
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
                        bookings = resource.data ?: emptyList()
                    )
                }
            }
            .collect { state ->
                _bookingUiState.update {
                    it.copy(
                        isLoading = state.isLoading,
                        errorMessage = state.errorMessage,
                        bookings = state.bookings
                    )
                }
            }
        _bookingUiState.update { it.copy(isLoading = false) }
    }

    fun createBooking(bookingRequest: BookingRequest) = viewModelScope.launch {
        bookingRepository.createBooking(
            bookingDate = bookingRequest.bookingDate,
            bookingTime = bookingRequest.bookingTime,
            frequency = bookingRequest.frequency,
            instructions = bookingRequest.additionalInstructions,
            address = bookingRequest.address
        )
            .collectLatest { resource ->
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
                                isSuccess = resource.data?.message.toString(),
                                booking = resource.data?.booking
                            )
                        }
                        fetchBookings()
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
        bookingRepository.updateBookingStatus(bookingId, updateBookingStatus.status)
            .collectLatest { resource ->
                when (resource) {
                    is Resource.Error -> {
                        _bookingUpdateUiState.update {
                            it.copy(
                                isLoading = false,
                                errorMessage = resource.message.toString()
                            )
                        }
                    }

                    is Resource.Loading -> {
                        _bookingUpdateUiState.update {
                            it.copy(isLoading = true)
                        }
                    }

                    is Resource.Success -> {
                        _bookingUpdateUiState.update {
                            it.copy(
                                isLoading = false,
                                successMessage = resource.data.toString()
                            )
                        }
                        fetchBookings()
                        fetchBookingAssignments(bookingId)
                        fetchCleanerAssignments()
                    }
                }
            }
    }

    fun deleteBooking(bookingId: String) = viewModelScope.launch {
        bookingRepository.deleteBooking(bookingId)
            .collect { resource ->
                when (resource) {
                    is Resource.Error -> {
                        _bookingUpdateUiState.update {
                            it.copy(
                                isLoading = false,
                                errorMessage = resource.message.toString()
                            )
                        }
                    }

                    is Resource.Loading -> {
                        _bookingUpdateUiState.update {
                            it.copy(isLoading = true)
                        }
                    }

                    is Resource.Success -> {
                        _bookingUpdateUiState.update {
                            it.copy(
                                isLoading = false,
                                successMessage = resource.data.toString()
                            )
                        }
                        fetchBookings()
                    }
                }
            }
    }

    fun assignBooking(
        bookingId: String,
        cleanerId: String
    ) = viewModelScope.launch {
        Log.d("assignBooking", "Assigning bookingId: $bookingId to cleanerId: $cleanerId")
        bookingRepository.assignBooking(
            bookingId = bookingId,
            cleanerId = cleanerId
        )
            .map { resource ->
                Log.d("assignBooking", "Resource state: $resource")
                when (resource) {
                    is Resource.Error -> {
                        Log.e("assignBooking", "Error: ${resource.message}")

                        BookingAssignmentUpdateUiState(
                            isLoading = false,
                            errorMessage = resource.message.toString()
                        )
                    }

                    is Resource.Loading -> {
                        Log.d("assignBooking", "Loading...")
                        BookingAssignmentUpdateUiState(
                            isLoading = true
                        )
                    }

                    is Resource.Success -> {
                        Log.d("assignBooking", "Success: ${resource.data}")

                    BookingAssignmentUpdateUiState(
                        isLoading = false,
                        successMessage = resource.data.toString()
                    )
                }
                }
            }
            .collectLatest { state ->
                Log.d("assignBooking", "Collected state: $state")
                _bookingAssignmentUpdateUiState.update {
                    it.copy(
                        isLoading = state.isLoading,
                        errorMessage = state.errorMessage,
                        successMessage = state.successMessage
                    )
                }
                if (!state.isLoading && state.successMessage.isNotEmpty()) {
                    fetchBookingAssignments(bookingId)
                }
            }
    }

    fun fetchBookingAssignments(bookingId: String) = viewModelScope.launch {
        bookingRepository.getAssignedBookings(bookingId)
            .map { resource ->
                when (resource) {
                    is Resource.Error -> BookingAssignmentUiState(
                        isLoading = false,
                        errorMessage = resource.message.toString()
                    )

                    is Resource.Loading -> BookingAssignmentUiState(
                        isLoading = true
                    )

                    is Resource.Success -> BookingAssignmentUiState(
                        isLoading = false,
                        assignedBookings = resource.data
                    )
                }
            }
            .collect { state ->
                _bookingAssignmentState.update {
                    it.copy(
                        isLoading = state.isLoading,
                        errorMessage = state.errorMessage,
                        assignedBookings = state.assignedBookings
                    )
                }
            }
    }

    fun fetchCleanerAssignments() = viewModelScope.launch {
        bookingRepository.getCleanerAssignments()
            .map { resource ->
                when (resource) {
                    is Resource.Error -> BookingAssignmentUiState(
                        isLoading = false,
                        errorMessage = resource.message.toString()
                    )

                    is Resource.Loading -> BookingAssignmentUiState(
                        isLoading = true
                    )

                    is Resource.Success -> BookingAssignmentUiState(
                        isLoading = false,
                        assignedBookings = resource.data
                    )
                }
            }
            .collect { state ->
                _bookingAssignmentState.update {
                    it.copy(
                        isLoading = state.isLoading,
                        errorMessage = state.errorMessage,
                        assignedBookings = state.assignedBookings
                    )
                }
            }
    }

    fun onStatusSwap() {
        viewModelScope.launch {
            _bookingAssignmentState.value = bookingAssignmentUiState.value.copy(
                assignedBookings = _bookingAssignmentState.value.assignedBookings
                    ?.sortedBy { it.bookingAssignment.bookingStatus }
            )
        }
    }
}