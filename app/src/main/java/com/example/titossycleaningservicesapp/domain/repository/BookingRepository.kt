package com.example.titossycleaningservicesapp.domain.repository

import com.example.titossycleaningservicesapp.core.Resource
import com.example.titossycleaningservicesapp.domain.models.Frequency
import com.example.titossycleaningservicesapp.domain.models.requests.booking.BookingStatus
import com.example.titossycleaningservicesapp.domain.models.ui_models.Booking
import kotlinx.coroutines.flow.Flow

interface BookingRepository {
    fun getBookings() : Flow<Resource<List<Booking>>>
    fun getBookingById(id: String) : Flow<Resource<Booking>>
    fun searchBooking(query: String): Flow<Resource<List<Booking>>>
    fun createBooking(
        bookingDate: String,
        bookingTime: String,
        frequency: Frequency,
        instructions: String?,
        address: String?
    ) : Flow<Resource<Booking>>
    fun updateBooking(
        bookingId: String,
        bookingDate: String,
        bookingTime: String,
        frequency: Frequency,
        instructions: String?,
        address: String?
    ) :Flow<Resource<String>>
    fun updateBookingStatus(bookingId: String,bookingStatus: BookingStatus) : Flow<Resource<String>>
    fun deleteBooking(bookingId: String) : Flow<Resource<String>>
}