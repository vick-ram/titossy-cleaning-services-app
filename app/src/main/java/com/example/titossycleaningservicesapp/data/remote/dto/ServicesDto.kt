package com.example.titossycleaningservicesapp.data.remote.dto

import com.example.titossycleaningservicesapp.data.local.database.entities.ServiceAddonEntity
import com.example.titossycleaningservicesapp.data.local.database.entities.ServiceEntity
import com.example.titossycleaningservicesapp.data.local.database.entities.ServiceWithAddOns

data class ServiceDto(
    val id: String,
    val name: String,
    val description: String,
    val price: String,
    val imageUrl: String,
    val addOns: List<ServiceAddOnDto>? = null,
    val createdAt: String,
    val updatedAt: String
) {
    fun toServiceWithAddOns() = ServiceWithAddOns(
        service = ServiceEntity(
            id = id,
            name = name,
            description = description,
            price = price,
            image = imageUrl,
            createdAt = createdAt,
            updatedAt = updatedAt
        ),
        addOns = addOns?.map { it.toServiceAddOn() }
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
    fun toServiceAddOn() = ServiceAddonEntity(
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