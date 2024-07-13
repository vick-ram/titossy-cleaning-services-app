package com.example.titossycleaningservicesapp.data.remote.dto

import com.example.titossycleaningservicesapp.domain.models.PaymentMethod
import com.example.titossycleaningservicesapp.domain.models.PaymentStatus
import com.example.titossycleaningservicesapp.domain.models.ui_models.CustomerPayment
import java.time.LocalDateTime

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
        amount.toBigDecimal(),
        paymentMethod?.let { PaymentMethod.valueOf(it) },
        phoneNumber,
        transactionCode,
        PaymentStatus.valueOf(paymentStatus),
        LocalDateTime.parse(createdAt),
        LocalDateTime.parse(updatedAt)
    )
}
