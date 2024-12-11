package com.example.titossycleaningservicesapp.domain.models.ui_models

import com.example.titossycleaningservicesapp.domain.models.ApprovalStatus
import java.time.LocalDateTime
import java.util.UUID

data class Customer(
    val id: String,
    val fullName: String,
    val phone: String,
    val address: String?,
    val email: String,
    val password: String,
    val status: ApprovalStatus,
    val createdAt: LocalDateTime,
    val updatedAt: LocalDateTime
)

data class CustomerState(
    val isLoading: Boolean = false,
    val customer: Customer? = null,
    val error: String? = null,
    val customers: List<Customer>? = null
)
