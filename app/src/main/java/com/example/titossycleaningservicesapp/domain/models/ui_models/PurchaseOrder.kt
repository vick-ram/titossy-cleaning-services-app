package com.example.titossycleaningservicesapp.domain.models.ui_models

import java.math.BigDecimal

data class PurchaseOrder(
    val purchaseOrderId: String,
    val employee: String,
    val supplier: String,
    val orderDate: String,
    val expectedDate: String,
    val purchaseOrderItems: List<PurchaseOrderItem>? = null,
    val totalAmount: BigDecimal,
    val orderStatus: String,
    val createdAt: String,
    val updatedAt: String
)


data class PurchaseOrderItem(
    val purchaseOrderItemId: String,
    val product: String,
    val quantity: Int,
    val unitPrice: String,
    val subtotal: String
)

