package com.example.titossycleaningservicesapp.domain.models.ui_models

import com.example.titossycleaningservicesapp.core.dateTimeUiFormat
import com.example.titossycleaningservicesapp.domain.models.PaymentStatus
import java.math.BigDecimal
import java.text.DecimalFormat
import java.time.LocalDateTime

data class CustomerPayment(
    val paymentId: String,
    val bookingId: String,
    val amount: BigDecimal,
    val paymentMethod: String?,
    val phoneNumber: String,
    val transactionCode: String,
    val paymentStatus: PaymentStatus,
    val createdAt: LocalDateTime,
    val updatedAt: LocalDateTime
) {
    val totalAmount: String
        get() {
            val decimalFormat = DecimalFormat("#,###.00")
            return decimalFormat.format(amount)
        }
    val paymentDate: String
        get() = createdAt.format(dateTimeUiFormat)
}

data class CustomerPaymentUIState(
    val isLoading: Boolean = false,
    val successMessage: String = "",
    val errorMessage: String = "",
    val customerPayment: CustomerPayment? = null,
    val customerPayments: List<CustomerPayment>? = null,
)
