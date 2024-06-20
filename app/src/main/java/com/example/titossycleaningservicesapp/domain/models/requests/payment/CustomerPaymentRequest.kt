package com.example.titossycleaningservicesapp.domain.models.requests.payment

data class CustomerPaymentRequest(
    val bookingId: String,
    val phoneNumber: String,
    val transactionId: String,
)

data class CustomerPaymentStatusUpdate(
    val paymentStatus: String
)
