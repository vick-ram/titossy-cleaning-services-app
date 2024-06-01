package com.example.titossycleaningservicesapp.domain.models.ui_models

import java.math.BigDecimal
import java.util.UUID

/*
data class CartItem(
    val addOns: CartAddOn? = null,
    val customerId: UUID,
    val service: CartService? = null,
    val total: BigDecimal
)*/

data class CartItem(
    val id: String,
    val name: String,
    val price: BigDecimal,
    val thumbnail: String,
    val total: BigDecimal
)
