package com.example.titossycleaningservicesapp.domain.models.ui_models

import com.example.titossycleaningservicesapp.domain.models.Frequency
import com.example.titossycleaningservicesapp.domain.models.requests.booking.BookingStatus
import java.time.LocalDateTime

data class Booking(
    val bookingId: String,
    val customer: String,
    val bookingDateTime: LocalDateTime,
    val frequency: Frequency,
    val additionalInstructions: String? = null,
    val address: String?,
    val bookingServiceAddons: List<BookingServiceAddOns>?,
    val totalAmount: String,
    val paid: Boolean,
    val bookingStatus: BookingStatus,
    val createdAt: String,
)

data class BookingServiceAddOns(
    val service: String?,
    val serviceAddOn: String?,
    val quantity: Int,
    val subtotal: String,
)

data class BookingUiState(
    val isLoading: Boolean = false,
    val bookings: List<Booking>? = null,
    val booking: Booking? = null,
    val errorMessage: String = "",
    val isSuccess: String = ""
)
