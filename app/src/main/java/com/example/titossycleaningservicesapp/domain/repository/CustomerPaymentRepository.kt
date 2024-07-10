package com.example.titossycleaningservicesapp.domain.repository

import com.example.titossycleaningservicesapp.core.Resource
import com.example.titossycleaningservicesapp.domain.models.ui_models.CustomerPayment
import kotlinx.coroutines.flow.Flow

interface CustomerPaymentRepository {
    fun makePayment(
        bookingId: String,
        phoneNumber: String,
        transactionCode: String
    ) : Flow<Resource<String>>
    fun updatePayment(
        paymentId: String,
        bookingId: String,
        phoneNumber: String,
        transactionCode: String
    ) : Flow<Resource<String>>
    fun updatePaymentStatus(paymentId: String,status: String) : Flow<Resource<String>>
    fun getCustomerPayments() : Flow<Resource<List<CustomerPayment>>>
    fun deletePayment(paymentId: String) : Flow<Resource<String>>
}