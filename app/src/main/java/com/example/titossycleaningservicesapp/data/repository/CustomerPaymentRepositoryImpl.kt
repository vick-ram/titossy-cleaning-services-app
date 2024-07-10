package com.example.titossycleaningservicesapp.data.repository

import com.example.titossycleaningservicesapp.core.FileUtils
import com.example.titossycleaningservicesapp.core.Resource
import com.example.titossycleaningservicesapp.data.remote.api.ApiService
import com.example.titossycleaningservicesapp.domain.models.requests.payment.CustomerPaymentRequest
import com.example.titossycleaningservicesapp.domain.models.requests.payment.CustomerPaymentStatusUpdate
import com.example.titossycleaningservicesapp.domain.models.ui_models.CustomerPayment
import com.example.titossycleaningservicesapp.domain.repository.CustomerPaymentRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class CustomerPaymentRepositoryImpl @Inject constructor(
    private val apiService: ApiService
) : CustomerPaymentRepository {
    override fun makePayment(
        bookingId: String,
        phoneNumber: String,
        transactionCode: String
    ): Flow<Resource<String>> {
        return flow {
            emit(Resource.Loading)
            val response = apiService.createPayment(
                CustomerPaymentRequest(
                    bookingId = bookingId,
                    phoneNumber = phoneNumber,
                    transactionCode = transactionCode
                )
            )
            when(response.status) {
                "success" -> {
                    val message = response.message
                    message?.let { emit(Resource.Success(it)) }
                }
                "error" -> {
                    if (response.error != null) {
                        val error = FileUtils.createErrorMessage(response.error)
                        throw Exception(error)
                    }
                }
            }
        }.catch { exception ->
            exception.printStackTrace()
            emit(Resource.Error(exception.message.toString()))
        }
    }

    override fun updatePayment(
        paymentId: String,
        bookingId: String,
        phoneNumber: String,
        transactionCode: String
    ): Flow<Resource<String>> {
        return flow {
            emit(Resource.Loading)
            val response = apiService.updateCustomerPayment(
                id = paymentId,
                CustomerPaymentRequest(
                    phoneNumber = phoneNumber,
                    bookingId = bookingId,
                    transactionCode = transactionCode
                )
            )
            when(response.status) {
                "success" -> {
                    val message = response.message
                    message?.let { emit(Resource.Success(it)) }
                }
                "error" -> {
                    if (response.error != null) {
                        val error = FileUtils.createErrorMessage(response.error)
                        throw Exception(error)
                    }
                }
            }
        }.catch { exception ->
            exception.printStackTrace()
            emit(Resource.Error(exception.message.toString()))
        }
    }

    override fun updatePaymentStatus(paymentId: String,status: String): Flow<Resource<String>> {
        return flow {
            emit(Resource.Loading)
            val response = apiService.updatePaymentStatus(
                paymentId = paymentId,
                CustomerPaymentStatusUpdate(
                    paymentStatus = status
                )
            )

            when(response.status) {
                "success" -> {
                    val customerPaymentUpdate = response.message
                    customerPaymentUpdate?.let { emit(Resource.Success(it)) }
                }
                "error" -> {
                    if (response.error != null) {
                        val error = FileUtils.createErrorMessage(response.error)
                        throw Exception(error)
                    }
                }
            }
        }.catch { exception ->
            exception.printStackTrace()
            emit(Resource.Error(exception.message.toString()))
        }
    }

    override fun getCustomerPayments(): Flow<Resource<List<CustomerPayment>>> {
        return flow {
            emit(Resource.Loading)
            val response = apiService.getCustomerPayment()
            when(response.status) {
                "success" -> {
                    val customerPayments = response.data?.map { it.toCustomerPayment() }
                    customerPayments?.let { emit(Resource.Success(it)) }
                }
                "error" -> {
                    if (response.error != null) {
                        val error = FileUtils.createErrorMessage(response.error)
                        throw Exception(error)
                    }
                }
            }
        }.catch { exception ->
            exception.printStackTrace()
            emit(Resource.Error(exception.message.toString()))
        }
    }

    override fun deletePayment(paymentId: String): Flow<Resource<String>> {
        return flow {
            emit(Resource.Loading)
            val response = apiService.deletePayment(paymentId)

            when(response.status) {
                "success" -> {
                    val customerPaymentUpdate = response.data
                    customerPaymentUpdate?.let { emit(Resource.Success(it)) }
                }
                "error" -> {
                    if (response.error != null) {
                        val error = FileUtils.createErrorMessage(response.error)
                        throw Exception(error)
                    }
                }
            }
        }.catch { exception ->
            exception.printStackTrace()
            emit(Resource.Error(exception.message.toString()))
        }
    }
}