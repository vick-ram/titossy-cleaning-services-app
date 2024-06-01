package com.example.titossycleaningservicesapp.domain.models.ui_models

import java.math.BigDecimal

data class CartService(
    val name: String = "",
    val price: BigDecimal = BigDecimal.ZERO,
    val thumbnail: String? = null
)