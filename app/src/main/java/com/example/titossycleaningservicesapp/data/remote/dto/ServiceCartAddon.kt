package com.example.titossycleaningservicesapp.data.remote.dto

import com.example.titossycleaningservicesapp.domain.models.ui_models.CartAddOn

data class ServiceCartAddon(
    val name: String,
    val price: String,
    val thumbnail: String
) {
    fun toCartAddOn() = CartAddOn(
        name, price.toBigDecimal(), thumbnail
    )
}