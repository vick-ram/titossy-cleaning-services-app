package com.example.titossycleaningservicesapp.data.remote.dto

import com.example.titossycleaningservicesapp.domain.models.OrderStatus
import com.example.titossycleaningservicesapp.domain.models.ui_models.PurchaseOrder
import com.example.titossycleaningservicesapp.domain.models.ui_models.PurchaseOrderItem
import java.math.BigDecimal
import java.time.LocalDate

data class PurchaseOrderDto(
    val purchaseOrderId: String,
    val employee: String,
    val supplier: String,
    val expectedDate: String,
    val purchaseOrderItems: List<PurchaseOrderItemDto>? = null,
    val totalAmount: BigDecimal,
    val paid: Boolean,
    val orderStatus: String,
    val createdAt: String,
    val updatedAt: String
) {
    fun toPurchaseOrder() = PurchaseOrder(
        purchaseOrderId = purchaseOrderId,
        employee = employee,
        supplier = supplier,
        expectedDate = LocalDate.parse(expectedDate),
        purchaseOrderItems = purchaseOrderItems?.map { it.toPurchaseOrderItem() },
        totalAmount = totalAmount,
        paid = paid,
        orderStatus = OrderStatus.valueOf(orderStatus)
    )
}


data class PurchaseOrderItemDto(
    val purchaseOrderItemId: String,
    val product: String,
    val quantity: Int,
    val unitPrice: String,
    val subtotal: String
) {
    fun toPurchaseOrderItem() = PurchaseOrderItem(
        purchaseOrderItemId = purchaseOrderItemId,
        product = product,
        quantity = quantity,
        unitPrice = unitPrice.toBigDecimal(),
        subtotal = subtotal.toBigDecimal()
    )
}

data class UpdateOrderStatus(
    val status: OrderStatus
)