package com.example.titossycleaningservicesapp.data.remote.dto

import com.example.titossycleaningservicesapp.domain.models.ui_models.CartService

data class ServiceCartServiceDto(
    val name: String,
    val price: String,
    val thumbnail: String
) {
    fun toCartService() = CartService(
        name, price.toBigDecimal(), thumbnail
    )
}