package com.example.titossycleaningservicesapp.domain.repository

import com.example.titossycleaningservicesapp.core.Resource
import com.example.titossycleaningservicesapp.domain.models.ui_models.CartItem
import com.example.titossycleaningservicesapp.domain.models.ui_models.Service
import com.example.titossycleaningservicesapp.domain.models.ui_models.ServiceAddOn
import kotlinx.coroutines.flow.Flow
import java.util.UUID

interface CartRepository {
    suspend fun addServiceToCart(serviceId: UUID, quantity: Int) : Flow<Resource<String?>>
    suspend fun addServiceAddonToCart(serviceAddonId: UUID, quantity: Int) :Flow<Resource<String?>>
    fun removeServiceFromCart(addonId: UUID) : Flow<Resource<String?>>
    fun getServicesInCart(): Flow<Resource<List<CartItem>>>
    fun clearCart() : Flow<Resource<String?>>
}