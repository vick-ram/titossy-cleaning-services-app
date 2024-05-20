package com.example.titossycleaningservicesapp.domain.models.requests.customer

import java.util.UUID

data class CustomerAddressRequest(
    val customerId: UUID,
    val street: String,
    val city: String,
    val state: String,
    val zip: String
)