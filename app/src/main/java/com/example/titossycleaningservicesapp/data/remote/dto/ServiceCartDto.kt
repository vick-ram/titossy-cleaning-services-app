package com.example.titossycleaningservicesapp.data.remote.dto

import com.example.titossycleaningservicesapp.domain.models.ui_models.CartItem
import java.util.UUID

data class ServiceCartDto(
    val customerId: String,
    val addOns: ServiceCartAddon? = null,
    val service: ServiceCartServiceDto? = null,
    val total: String
) {
    fun toCartItem(): CartItem {
        return when {
            service != null -> {
                CartItem.ServiceCartItem(
                    id = UUID.fromString(service.id),
                    name = service.name,
                    price = service.price.toBigDecimal(),
                    thumbnail = service.thumbnail,
                    quantity = service.quantity,
                    total = total.toBigDecimal()
                )
            }

            addOns != null -> {
                CartItem.ServiceAddonCartItem(
                    id = UUID.fromString(addOns.id),
                    name = addOns.name,
                    price = addOns.price.toBigDecimal(),
                    thumbnail = addOns.thumbnail,
                    quantity = addOns.quantity,
                    total = total.toBigDecimal()
                )
            }

            else -> throw IllegalStateException("Invalid CartItemDto: Missing service or addOns")
        }
    }
}