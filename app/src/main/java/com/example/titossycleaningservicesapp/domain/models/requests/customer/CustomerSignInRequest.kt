package com.example.titossycleaningservicesapp.domain.models.requests.customer

data class CustomerSignInRequest(
    val username: String? = null,
    val email: String? = null,
    val password: String
)