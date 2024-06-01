package com.example.titossycleaningservicesapp.domain.models.requests.employee

import com.example.titossycleaningservicesapp.domain.models.Availability

data class EmployeeSignInRequest(
    val email: String,
    val password: String
)

data class EmployeeAvailability(
    val availability: Availability
)