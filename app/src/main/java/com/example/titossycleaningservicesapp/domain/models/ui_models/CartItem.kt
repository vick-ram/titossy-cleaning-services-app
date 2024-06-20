package com.example.titossycleaningservicesapp.domain.models.ui_models

import java.math.BigDecimal
import java.util.UUID


public sealed class CartItem(
    open val id: UUID,
    open val name: String,
    open val price: BigDecimal,
    open val thumbnail: String,
    open val quantity: Int,
    open val total: BigDecimal
) {
    data class ServiceCartItem(
        override val id: UUID,
        override val name: String,
        override val price: BigDecimal,
        override val thumbnail: String,
        override val quantity: Int,
        override val total: BigDecimal
    ) : CartItem(id, name, price, thumbnail, quantity, total)
   data class ServiceAddonCartItem(
        override val id: UUID,
        override val name: String,
        override val price: BigDecimal,
        override val thumbnail: String,
        override val quantity: Int,
        override val total: BigDecimal
    ) : CartItem(id, name, price, thumbnail, quantity, total)
}
