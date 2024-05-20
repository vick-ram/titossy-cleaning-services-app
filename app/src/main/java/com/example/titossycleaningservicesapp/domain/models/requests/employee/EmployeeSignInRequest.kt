package com.example.titossycleaningservicesapp.domain.models.requests.employee

data class EmployeeSignInRequest(
    val email: String,
    val password: String
)