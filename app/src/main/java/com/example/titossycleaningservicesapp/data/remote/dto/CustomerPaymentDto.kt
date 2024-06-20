package com.example.titossycleaningservicesapp.data.remote.dto

import com.example.titossycleaningservicesapp.domain.models.ui_models.CustomerPayment

data class CustomerPaymentDto(
    val paymentId: String,
    val bookingId: String,
    val amount: String,
    val paymentMethod: String?,
    val phoneNumber: String,
    val transactionCode: String,
    val paymentStatus: String,
    val createdAt: String,
    val updatedAt: String
) {
    fun toCustomerPayment() = CustomerPayment(
        paymentId,
        bookingId,
        amount,
        paymentMethod,
        phoneNumber,
        transactionCode,
        paymentStatus,
        createdAt,
        updatedAt
    )
}
