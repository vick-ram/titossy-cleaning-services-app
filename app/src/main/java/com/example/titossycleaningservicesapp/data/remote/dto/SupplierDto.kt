package com.example.titossycleaningservicesapp.data.remote.dto

import com.example.titossycleaningservicesapp.data.local.database.entities.SupplierAddressEntity
import com.example.titossycleaningservicesapp.data.local.database.entities.SupplierEntity

data class SupplierDto(
    val id: String,
    val username: String,
    val fullName: String,
    val phone: String,
    val company: String,
    val address: SupplierAddressDto,
    val email: String,
    val password: String,
    val status: String,
    val createdAt: String,
    val updatedAt: String
) {
    fun toSupplierEntity() = SupplierEntity(
        id = id,
        username = username,
        fullName = fullName,
        phone = phone,
        company = company,
        address = address.toSupplierAddress(),
        email = email,
        password = password,
        status = status,
        createdAt = createdAt,
        updatedAt = updatedAt
    )
}

data class SupplierAddressDto(
    val county: String,
    val region: String,
    val postalCode: String
) {
    fun toSupplierAddress() = SupplierAddressEntity(
        county = county,
        region = region,
        postalCode = postalCode
    )
}