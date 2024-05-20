package com.example.titossycleaningservicesapp.domain.models.ui_models

import java.math.BigDecimal
import java.util.UUID

data class CartItem(
    val id: UUID,
    val cartId: UUID,
    val service: UUID,
    val serviceAddOn: UUID,
    val quantity: Int,
    val price: BigDecimal,
    val subtotal: BigDecimal
)
