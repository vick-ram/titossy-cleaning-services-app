package com.example.titossycleaningservicesapp.data.remote.dto

import com.example.titossycleaningservicesapp.domain.models.ui_models.Product
import com.example.titossycleaningservicesapp.domain.models.ui_models.ProductCart
import com.example.titossycleaningservicesapp.domain.models.ui_models.ProductInCart
import java.math.BigDecimal

data class ProductDto(
    val productId: String,
    val name: String,
    val description: String,
    val unitPrice: String,
    val image: String?,
    val stock: Int,
    val reorderLevel: Int,
    val sku: String,
    val createdAt: String,
    val updatedAt: String
) {
    fun toProduct() = Product(
        productId = productId,
        name = name,
        description = description,
        unitPrice = unitPrice,
        image = image,
        stock = stock,
        reorderLevel = reorderLevel,
        sku = sku
    )
}

data class ProductCartDto(
    val product: ProductCartRes,
    val quantity: Int
) {
    fun toProductCart() = ProductCart(
        product.toProductInCart(), quantity
    )
}

data class ProductCartRes(
    val name: String,
    val price: BigDecimal,
    val stock: Int,
) {
    fun toProductInCart() = ProductInCart(
        name, price, stock
    )
}

