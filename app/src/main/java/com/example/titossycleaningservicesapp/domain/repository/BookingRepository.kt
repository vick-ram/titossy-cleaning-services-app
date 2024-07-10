package com.example.titossycleaningservicesapp.domain.repository

import com.example.titossycleaningservicesapp.core.Resource
import com.example.titossycleaningservicesapp.data.repository.BookingCreationResult
import com.example.titossycleaningservicesapp.domain.models.BookingStatus
import com.example.titossycleaningservicesapp.domain.models.Frequency
import com.example.titossycleaningservicesapp.domain.models.ui_models.Booking
import com.example.titossycleaningservicesapp.domain.models.ui_models.BookingCleanerAssignment
import kotlinx.coroutines.flow.Flow

interface BookingRepository {
    fun getBookings() : Flow<Resource<List<Booking>>>
    fun getCustomerBookings() : Flow<Resource<List<Booking>>>
    fun getBookingById(id: String) : Flow<Resource<Booking>>
    fun searchBooking(query: String): Flow<Resource<List<Booking>>>
    fun createBooking(
        bookingDate: String,
        bookingTime: String,
        frequency: Frequency,
        instructions: String?,
        address: String?
    ) : Flow<Resource<BookingCreationResult>>
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

    /**Booking assignments**/
    fun assignBooking(
        bookingId: String,
        cleanerId: String
    ): Flow<Resource<String>>
    fun getAssignedBookings(bookingId: String) : Flow<Resource<List<BookingCleanerAssignment>>>
    fun getCleanerAssignments(): Flow<Resource<List<BookingCleanerAssignment>>>
}