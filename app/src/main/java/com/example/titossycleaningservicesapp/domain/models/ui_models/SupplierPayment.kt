package com.example.titossycleaningservicesapp.domain.models.ui_models

import com.example.titossycleaningservicesapp.domain.models.PaymentMethod
import com.example.titossycleaningservicesapp.domain.models.PaymentStatus
import java.math.BigDecimal
import java.time.LocalDateTime

data class SupplierPayment(
    val paymentId: String,
    val employee: String,
    val orderId: String,
    val supplier: String,
    val paymentDate: LocalDateTime,
    val amount: BigDecimal,
    val method: PaymentMethod,
    val paymentReference: String,
    val status: PaymentStatus,
)

data class SupplierPaymentUiState(
    val isLoading: Boolean = false,
    val supplierPayments: List<SupplierPayment>? = null,
    val errorMessage: String = ""
)

data class SupplierPaymentStatusUiState(
    val isLoading: Boolean = false,
    val successMessage: String = "",
    val errorMessage: String = ""
)
