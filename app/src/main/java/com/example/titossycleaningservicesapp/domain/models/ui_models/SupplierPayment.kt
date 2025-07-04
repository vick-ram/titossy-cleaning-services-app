package com.example.titossycleaningservicesapp.domain.models.ui_models

import android.icu.text.DecimalFormat
import com.example.titossycleaningservicesapp.core.dateTimeUiFormat
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
) {
    val formattedDate: String
        get() = paymentDate.format(dateTimeUiFormat)
    val formattedAmount: String
        get() = "Kshs. ${DecimalFormat("#,###.00").format(amount)}"
}

data class SupplierPaymentUiState(
    val isLoading: Boolean = false,
    val supplierPayments: List<SupplierPayment> = emptyList(),
    val errorMessage: String = ""
)

data class SupplierPaymentStatusUiState(
    val isLoading: Boolean = false,
    val successMessage: String = "",
    val errorMessage: String = ""
)
