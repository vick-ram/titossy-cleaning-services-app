package com.example.titossycleaningservicesapp.domain.models.ui_models

import java.math.BigDecimal
import java.util.UUID

data class Cart(
    val id: UUID,
    val customerId: UUID,
    val totalAmount: BigDecimal,
    val cartItems: List<CartItem> = emptyList(),
)
