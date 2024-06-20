package com.example.titossycleaningservicesapp.domain.models.requests.po

import com.example.titossycleaningservicesapp.domain.models.ui_models.Product
import com.example.titossycleaningservicesapp.domain.models.ui_models.ProductCart
import java.util.UUID

data class PurchaseOrderRequest(
    val supplierId: String,
    val orderDate: String,
    val expectedDate: String,
    val products: MutableList<PurchaseOrderItemRequest>? = null
)

data class PurchaseOrderItemRequest(
    val productId: UUID,
    val quantity: Int,
    val unitPrice: String
)

data class AddProductToCart(
    val productId: UUID,
    val quantity: Int
)

data class ProductUiState(
    val isLoading: Boolean = false,
    val successMessage: String = "",
    val errorMessage: String = ""
)

data class ProductDataUiState(
    val isLoading: Boolean = false,
    val products: List<Product>? = null,
    val errorMessage: String = ""
)

data class ProductCartUiState(
    val isLoading: Boolean = false,
    val products: List<ProductCart>? = null,
    val errorMessage: String = ""
)
