package com.example.titossycleaningservicesapp.domain.models.requests.booking

import com.example.titossycleaningservicesapp.domain.models.BookingStatus
import com.example.titossycleaningservicesapp.domain.models.Frequency
import java.util.UUID

data class BookingRequest(
    val bookingDate: String,
    val bookingTime: String,
    val frequency: Frequency,
    val additionalInstructions: String? = null,
    val address: String?
)

data class UpdateBookingStatus(
    val status: BookingStatus
)

data class AssignBooking(
    val bookingId: String,
    val cleanerId: UUID
)

