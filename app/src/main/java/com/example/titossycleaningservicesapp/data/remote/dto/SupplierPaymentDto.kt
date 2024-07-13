package com.example.titossycleaningservicesapp.data.remote.dto

import com.example.titossycleaningservicesapp.domain.models.PaymentMethod
import com.example.titossycleaningservicesapp.domain.models.PaymentStatus
import com.example.titossycleaningservicesapp.domain.models.ui_models.SupplierPayment
import java.time.LocalDateTime

data class SupplierPaymentDto(
    val paymentId: String,
    val employee: String,
    val orderId: String,
    val supplier: String,
    val paymentDate: String,
    val amount: String,
    val method: String,
    val paymentReference: String,
    val status: String,
    val updatedAt: String
) {
    fun toSupplierPayment() = SupplierPayment(
        paymentId = paymentId,
        employee = employee,
        orderId = orderId,
        supplier = supplier,
        paymentDate = LocalDateTime.parse(paymentDate),
        amount = amount.toBigDecimal(),
        method = PaymentMethod.valueOf(method),
        paymentReference = paymentReference,
        status = PaymentStatus.valueOf(status),
    )
}
