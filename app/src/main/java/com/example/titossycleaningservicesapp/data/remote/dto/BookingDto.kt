package com.example.titossycleaningservicesapp.data.remote.dto

import com.example.titossycleaningservicesapp.domain.models.Frequency
import com.example.titossycleaningservicesapp.domain.models.requests.booking.BookingStatus
import com.example.titossycleaningservicesapp.domain.models.ui_models.Booking
import com.example.titossycleaningservicesapp.domain.models.ui_models.BookingServiceAddOns
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

data class BookingDto(
    val bookingId: String,
    val customer: String,
    val bookingDateTime: String,
    val frequency: String,
    val additionalInstructions: String,
    val address: String,
    val bookingServiceAddOns: List<BookingServiceAddOnsDto>,
    val totalAmount: String,
    val paid: Boolean,
    val bookingStatus: String,
    val createdAt: String,
    val updatedAt: String,
) {
    fun toBooking(): Booking {
        return Booking(
            bookingId = bookingId,
            customer = customer,
            bookingDateTime = LocalDateTime.parse(
                bookingDateTime,
                DateTimeFormatter.ISO_LOCAL_DATE_TIME
            ),
            frequency = Frequency.valueOf(frequency),
            additionalInstructions = additionalInstructions,
            address = address,
            bookingServiceAddons = bookingServiceAddOns.map
            { it.toBookingServiceAddons() },
            totalAmount = totalAmount.toBigDecimal(),
            paid = paid,
            bookingStatus = BookingStatus.valueOf(bookingStatus),
        )
    }
}

data class BookingServiceAddOnsDto(
    val service: String?,
    val serviceAddOn: String? = null,
    val quantity: Int,
    val subtotal: String,
) {
    fun toBookingServiceAddons() = BookingServiceAddOns(
        service = service,
        serviceAddOn = serviceAddOn,
        quantity = quantity,
        subtotal = subtotal
    )
}
