package com.example.titossycleaningservicesapp.domain.repository

import android.content.Context
import android.net.Uri
import com.example.titossycleaningservicesapp.core.Resource
import com.example.titossycleaningservicesapp.domain.models.ui_models.Product
import com.example.titossycleaningservicesapp.domain.models.ui_models.ProductCart
import kotlinx.coroutines.flow.Flow

interface ProductRepository {

    fun createProduct(
        context: Context,
        uri: Uri,
        name: String,
        description: String,
        price: String,
        stock: String,
        reorderLevel: String
    ): Flow<Resource<String>>
    fun deleteProduct(productId: String) : Flow<Resource<String>>

    fun getAllProducts(): Flow<Resource<List<Product>>>

    fun addProductToPurchase(
        productId: String,
        quantity: Int
    ) : Flow<Resource<String>>
    fun deleteProductFromCart(
        productId: String
    ) : Flow<Resource<String>>
    fun getProductCart() : Flow<Resource<List<ProductCart>>>
}