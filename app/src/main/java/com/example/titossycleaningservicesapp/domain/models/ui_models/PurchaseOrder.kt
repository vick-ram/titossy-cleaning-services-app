package com.example.titossycleaningservicesapp.domain.models.ui_models

import android.icu.text.DecimalFormat
import com.example.titossycleaningservicesapp.core.dateUiFormat
import com.example.titossycleaningservicesapp.domain.models.OrderStatus
import java.math.BigDecimal
import java.time.LocalDate

data class PurchaseOrder(
    val purchaseOrderId: String,
    val employee: String,
    val supplier: String,
    val expectedDate: LocalDate,
    val purchaseOrderItems: List<PurchaseOrderItem>? = null,
    val totalAmount: BigDecimal,
    val paid: Boolean,
    val orderStatus: OrderStatus,
) {
    val formattedAmount: String
        get() = "Kshs. ${DecimalFormat("#,###.00").format(totalAmount)}"
    val formattedDate: String
        get() = expectedDate.format(dateUiFormat)
}


data class PurchaseOrderItem(
    val purchaseOrderItemId: String,
    val product: String,
    val quantity: Int,
    val unitPrice: BigDecimal,
    val subtotal: BigDecimal
) {
    val formattedUnitPrice: String
        get() = "Kshs. ${DecimalFormat("#,###.00").format(unitPrice)}"
    val formattedSubtotal: String
        get() = "Kshs. ${DecimalFormat("#,###.00").format(subtotal)}"
}

data class PurchaseOrderUiState(
    val isLoading: Boolean = false,
    val purchaseOrder: PurchaseOrder? = null,
    val purchaseOrders: List<PurchaseOrder>? = null,
    val successMessage: String = "",
    val errorMessage: String = ""
)

data class PurchaseOrderStatusUiState(
    val isLoading: Boolean = false,
    val successMessage: String = "",
    val errorMessage: String = ""
)

