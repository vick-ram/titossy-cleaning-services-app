package com.example.titossycleaningservicesapp.domain.repository

import com.example.titossycleaningservicesapp.domain.models.ui_models.Service

interface CartRepository {

    fun addServiceToCart(service: Service)

    fun removeServiceFromCart(service: Service)

    fun getServicesInCart(): List<Service>


    fun clearCart()
}