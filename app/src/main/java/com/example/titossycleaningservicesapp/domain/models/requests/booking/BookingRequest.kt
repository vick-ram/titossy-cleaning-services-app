package com.example.titossycleaningservicesapp.domain.models.requests.booking

import com.example.titossycleaningservicesapp.domain.models.Frequency

data class BookingRequest(
    val bookingDate: String,
    val bookingTime: String,
    val frequency: Frequency,
    val additionalInstructions: String? = null,
    val address: String?
)

data class UpdateBookingStatus(
    val bookingStatus: BookingStatus
)

enum class BookingStatus {
    PENDING, IN_PROGRESS, CANCELLED, COMPLETED
}
