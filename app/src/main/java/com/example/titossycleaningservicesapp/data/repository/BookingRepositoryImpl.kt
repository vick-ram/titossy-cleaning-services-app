package com.example.titossycleaningservicesapp.data.repository

import com.example.titossycleaningservicesapp.core.FileUtils
import com.example.titossycleaningservicesapp.core.Resource
import com.example.titossycleaningservicesapp.data.remote.api.ApiService
import com.example.titossycleaningservicesapp.domain.models.Frequency
import com.example.titossycleaningservicesapp.domain.models.requests.booking.BookingRequest
import com.example.titossycleaningservicesapp.domain.models.requests.booking.BookingStatus
import com.example.titossycleaningservicesapp.domain.models.requests.booking.UpdateBookingStatus
import com.example.titossycleaningservicesapp.domain.models.ui_models.Booking
import com.example.titossycleaningservicesapp.domain.repository.BookingRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class BookingRepositoryImpl @Inject constructor(
    private val apiService: ApiService
) : BookingRepository {
    override fun getBookings(): Flow<Resource<List<Booking>>> {
        return flow {
            emit(Resource.Loading)
            val response = apiService.getBookings()
            when (response.status) {
                "success" -> {
                    val bookings = response.data?.map { it.toBooking() }
                    bookings?.let { emit(Resource.Success(it)) }

                }

                "error" -> {
                    if (response.error != null) {
                        val errorMessage = FileUtils.createErrorMessage(response.error)
                        throw Exception(errorMessage)
                    }
                }
            }
        }.catch { e ->
            e.printStackTrace()
            emit(Resource.Error(e.localizedMessage ?: "An error occurred"))
        }
    }

    override fun searchBooking(query: String): Flow<Resource<List<Booking>>> {
        return flow {
            emit(Resource.Loading)
            val response = apiService.searchBookings(query)
            when (response.status) {
                "success" -> {
                    val bookings = response.data?.map { it.toBooking() }
                    bookings?.let { emit(Resource.Success(it)) }
                }

                "error" -> {
                    if (response.error != null) {
                        val errors = FileUtils.createErrorMessage(response.error)
                        throw Exception(errors)
                    }
                }
            }
        }.catch { exceptions ->
            emit(Resource.Error(exceptions.message.toString()))
        }
    }

    override fun getBookingById(id: String): Flow<Resource<Booking>> {
        return flow {
            emit(Resource.Loading)
            val response = apiService.getBookingById(id)
            when (response.status) {
                "success" -> {
                    val booking = response.data?.toBooking()
                    booking?.let { emit(Resource.Success(it)) }
                }

                "error" -> {
                    if (response.error != null) {
                        val errorMessage = FileUtils.createErrorMessage(response.error)
                        throw Exception(errorMessage)
                    }
                }
            }
        }.catch { e ->
            e.printStackTrace()
            emit(Resource.Error(e.message.toString()))
        }
    }

    override fun createBooking(
        bookingDate: String,
        bookingTime: String,
        frequency: Frequency,
        instructions: String?,
        address: String?
    ): Flow<Resource<Booking>> {
        return flow {
            emit(Resource.Loading)
            val bookingRequest = BookingRequest(
                bookingDate = bookingDate,
                bookingTime = bookingTime,
                frequency = frequency,
                additionalInstructions = instructions,
                address = address
            )
            val response = apiService.createBooking(bookingRequest)
            when (response.status) {
                "success" -> {
                    val booking = response.data?.toBooking()
                    booking?.let { emit(Resource.Success(it)) }
                }

                "error" -> {
                    if (response.error != null) {
                        val errorMessage = FileUtils.createErrorMessage(response.error)
                        throw Exception(errorMessage)
                    }
                }
            }
        }.catch { e ->
            emit(Resource.Error(e.message.toString()))
        }
    }

    override fun updateBooking(
        bookingId: String,
        bookingDate: String,
        bookingTime: String,
        frequency: Frequency,
        instructions: String?,
        address: String?
    ): Flow<Resource<String>> {
        return flow {
            emit(Resource.Loading)
            val bookingRequest = BookingRequest(
                bookingDate = bookingDate,
                bookingTime = bookingTime,
                frequency = frequency,
                additionalInstructions = instructions,
                address = address
            )
            val response = apiService.updateBooking(bookingId, bookingRequest)
            when (response.status) {
                "success" -> {
                    val message = response.data
                    message?.let { emit(Resource.Success(it)) }
                }

                "error" -> {
                    if (response.error != null) {
                        val errorMessage = FileUtils.createErrorMessage(response.error)
                        throw Exception(errorMessage)
                    }
                }
            }
        }.catch { e ->
            emit(Resource.Error(e.message.toString()))
        }
    }

    override fun updateBookingStatus(
        bookingId: String,
        bookingStatus: BookingStatus
    ): Flow<Resource<String>> {
        return flow {
            emit(Resource.Loading)
            val status = UpdateBookingStatus(bookingStatus)
            val response = apiService.updateBookingStatus(bookingId, status)
            when (response.status) {
                "success" -> {
                    val message = response.data
                    message?.let { emit(Resource.Success(it)) }
                }

                "error" -> {
                    if (response.error != null) {
                        val errorMessage = FileUtils.createErrorMessage(response.error)
                        throw Exception(errorMessage)
                    }
                }
            }
        }.catch { e ->
            e.printStackTrace()
            emit(Resource.Error(e.message.toString()))
        }
    }

    override fun deleteBooking(bookingId: String): Flow<Resource<String>> {
        return flow {
            emit(Resource.Loading)
            val response = apiService.deleteBooking(bookingId)
            when (response.status) {
                "success" -> {
                    val message = response.data
                    message?.let { emit(Resource.Success(it)) }
                }

                "error" -> {
                    if (response.error != null) {
                        val errorMessage = FileUtils.createErrorMessage(response.error)
                        throw Exception(errorMessage)
                    }
                }
            }
        }.catch { e ->
            emit(Resource.Error(e.message.toString()))
        }
    }
}