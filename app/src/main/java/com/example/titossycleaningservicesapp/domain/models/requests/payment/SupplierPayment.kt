package com.example.titossycleaningservicesapp.domain.models.requests.payment

data class SupplierPaymentRequest(
    val orderId: String,
    val method: String
)