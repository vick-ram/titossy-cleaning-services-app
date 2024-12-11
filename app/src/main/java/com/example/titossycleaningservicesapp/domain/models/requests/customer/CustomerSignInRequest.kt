package com.example.titossycleaningservicesapp.domain.models.requests.customer

data class CustomerSignInRequest(
    val email: String,
    val password: String
)