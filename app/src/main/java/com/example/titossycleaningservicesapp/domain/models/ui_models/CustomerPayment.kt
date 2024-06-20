package com.example.titossycleaningservicesapp.domain.models.ui_models

import java.text.DecimalFormat

data class CustomerPayment(
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
    val totalAmount: String
        get() {
            val decimalFormat = DecimalFormat("#,###.##")
            return decimalFormat.format(amount)
        }
}

data class CustomerPaymentUIState(
    val isLoading: Boolean = false,
    val successMessage: String = "",
    val errorMessage: String = "",
    val customerPayment: CustomerPayment? = null,
    val customerPayments: List<CustomerPayment> = emptyList(),
)
