package com.example.titossycleaningservicesapp.data.remote.dto

import com.example.titossycleaningservicesapp.data.local.database.entities.ServiceAddonEntity
import com.example.titossycleaningservicesapp.data.local.database.entities.ServiceEntity

data class ServiceDto(
    val id: String,
    val name: String,
    val description: String,
    val price: String,
    val imageUrl: String,
    val createdAt: String,
    val updatedAt: String
){
    fun toServiceEntity() = ServiceEntity(
        id = id,
        name = name,
        description = description,
        price = price,
        image = imageUrl,
        createdAt = createdAt,
        updatedAt = updatedAt
    )
}

data class ServiceAddOnDto(
    val id: String,
    val serviceId: String,
    val name: String,
    val description: String,
    val price: String,
    val imageUrl: String,
    val createdAt: String,
    val updatedAt: String
) {
    fun toServiceAddonEntity() = ServiceAddonEntity(
        id = id,
        serviceId = serviceId,
        name = name,
        description = description,
        price = price,
        image = imageUrl,
        createdAt = createdAt,
        updatedAt = updatedAt
    )
}