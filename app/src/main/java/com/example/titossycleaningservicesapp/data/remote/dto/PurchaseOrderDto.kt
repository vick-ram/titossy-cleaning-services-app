package com.example.titossycleaningservicesapp.data.remote.dto

import java.math.BigDecimal

data class PurchaseOrderDto(
    val purchaseOrderId: String,
    val employee: String,
    val supplier: String,
    val orderDate: String,
    val expectedDate: String,
    val purchaseOrderItems: List<PurchaseOrderItemDto>? = null,
    val totalAmount: BigDecimal,
    val orderStatus: String,
    val createdAt: String,
    val updatedAt: String
)


data class PurchaseOrderItemDto(
    val purchaseOrderItemId: String,
    val product: String,
    val quantity: Int,
    val unitPrice: String,
    val subtotal: String
)