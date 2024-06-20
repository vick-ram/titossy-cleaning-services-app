package com.example.titossycleaningservicesapp.domain.models.ui_models

import java.math.BigDecimal

data class Product(
    val productId: String,
    val name: String,
    val description: String,
    val unitPrice: String,
    val image: String?,
    val stock: Int,
    val reorderLevel: Int,
    val sku: String,
)

data class ProductCart(
    val product: ProductInCart,
    val quantity: Int
)

data class ProductInCart(
    val name: String,
    val price: BigDecimal,
    val stock: Int,
)
