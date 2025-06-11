package com.example.titossycleaningservicesapp.domain.models.ui_models

import android.icu.text.DecimalFormat
import com.example.titossycleaningservicesapp.core.dateTimeUiFormat
import com.example.titossycleaningservicesapp.domain.models.BookingStatus
import com.example.titossycleaningservicesapp.domain.models.Frequency
import java.math.BigDecimal
import java.time.LocalDateTime

data class Booking(
    val bookingId: String,
    val customer: String,
    val bookingDateTime: LocalDateTime,
    val frequency: Frequency,
    val additionalInstructions: String,
    val address: String,
    val bookingServiceAddons: List<BookingServiceAddOns>,
    val totalAmount: BigDecimal,
    val paid: Boolean,
    val bookingStatus: BookingStatus,
) {
    val amount: String
        get() = DecimalFormat("#,###.00").format(totalAmount)
    val paidString: String
        get() = if (paid) "Paid" else "Not Paid"
    val formattedDate: String
        get() = bookingDateTime.format(dateTimeUiFormat)
}

data class BookingServiceAddOns(
    val service: String?,
    val serviceAddOn: String?,
    val quantity: Int,
    val subtotal: String,
) {
    val addon: String
        get() = serviceAddOn ?: ""
    val total: String
        get() = DecimalFormat("#,###.00").format(subtotal.toBigDecimal())
}

data class BookingUiState(
    val isLoading: Boolean = false,
    val bookings: List<Booking> = emptyList(),
    val booking: Booking? = null,
    val errorMessage: String = "",
    val isSuccess: String = ""
)

data class BookingUpdateUiState(
    val isLoading: Boolean = false,
    val successMessage: String = "",
    val errorMessage: String = ""
)

data class BookingAssignmentUiState(
    val isLoading: Boolean = false,
    val assignedBookings: List<BookingCleanerAssignment>? = null,
    val errorMessage: String = "",
    val isSuccess: String = ""
)

data class BookingAssignmentUpdateUiState(
    val isLoading: Boolean = false,
    val successMessage: String = "",
    val errorMessage: String = ""
)

data class BookingCleanerAssignment(
    val bookingAssignment: BookingAssignment,
    val cleaner: String,
    val assignedBy: String,
    val assignedDate: LocalDateTime
) {
    val formattedDate: String
        get() = assignedDate.format(dateTimeUiFormat)
}

data class BookingAssignment(
    val bookingId: String,
    val customer: String,
    val service: String,
    val instructions: String,
    val address: String?,
    val bookingStatus: BookingStatus
)

