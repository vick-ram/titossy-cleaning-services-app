package com.example.titossycleaningservicesapp.domain.models.requests.customer

data class CustomerSignUpRequest(
    val firstName: String,
    val lastName: String,
    val phone: String,
    val email: String,
    val password: String
)