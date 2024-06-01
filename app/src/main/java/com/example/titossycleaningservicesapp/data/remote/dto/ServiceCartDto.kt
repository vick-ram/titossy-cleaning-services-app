package com.example.titossycleaningservicesapp.data.remote.dto

import com.example.titossycleaningservicesapp.domain.models.ui_models.CartItem

data class ServiceCartDto(
    val addOns: ServiceCartAddon? = null,
    val customerId: String,
    val service: ServiceCartServiceDto? = null,
    val total: String
) {
    fun toCartItem(): CartItem {
        val (name, price, thumbnail) = when {
            service != null -> Triple(service.name, service.price.toBigDecimal(), service.thumbnail)
            addOns != null -> Triple(addOns.name, addOns.price.toBigDecimal(), addOns.thumbnail)
            else -> throw IllegalStateException("Invalid CartItemDto: Missing service or addOns")
        }
        return CartItem(
            id = customerId,
            name = name,
            price = price,
            thumbnail = thumbnail,
            total = total.toBigDecimal()
        )
    }
}