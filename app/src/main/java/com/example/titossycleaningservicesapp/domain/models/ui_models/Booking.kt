package com.example.titossycleaningservicesapp.domain.models.ui_models

import android.icu.text.DecimalFormat
import com.example.titossycleaningservicesapp.domain.models.Frequency
import com.example.titossycleaningservicesapp.domain.models.requests.booking.BookingStatus
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
    val bookings: List<Booking>? = null,
    val booking: Booking? = null,
    val errorMessage: String = "",
    val isSuccess: String = ""
)
