package com.example.titossycleaningservicesapp.domain.models.requests.cart

import com.example.titossycleaningservicesapp.domain.models.ui_models.CartItem
import java.util.UUID

data class AddServiceToCart(
    val serviceId: UUID,
    val quantity: Int
)

data class AddServiceAddonToCart(
    val serviceAddon: UUID,
    val quantity: Int
)

data class CartUiState(
    val loading: Boolean = false,
    val message: String = "",
    val error: String = ""
)
data class CartUiDataState(
    val loading: Boolean = false,
    val cartItems: List<CartItem> = emptyList(),
    val error: String = ""
)

data class CartClearState(
    val loading: Boolean = false,
    val message: String = "",
    val error: String = ""
)