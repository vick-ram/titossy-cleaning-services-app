package com.example.titossycleaningservicesapp.domain.models.requests.employee

import com.example.titossycleaningservicesapp.domain.models.Availability

data class EmployeeStatusUpdate(
    val availability: Availability
)