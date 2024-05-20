package com.example.titossycleaningservicesapp.domain.models.requests.supplier

data class SupplierAddressRequest(
    val street: String,
    val city: String,
    val state: String,
    val zip: String
)