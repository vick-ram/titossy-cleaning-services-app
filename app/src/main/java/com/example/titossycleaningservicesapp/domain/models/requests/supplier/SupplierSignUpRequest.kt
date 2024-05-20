package com.example.titossycleaningservicesapp.domain.models.requests.supplier

import com.example.titossycleaningservicesapp.data.remote.dto.SupplierAddressDto

data class SupplierSignUpRequest(
    val username: String,
    val firstName: String,
    val lastName: String,
    val phone: String,
    val company: String,
    val address: SupplierAddressDto,
    val email: String,
    val password: String
)