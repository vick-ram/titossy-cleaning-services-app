package com.example.titossycleaningservicesapp.domain.models.ui_models

import com.example.titossycleaningservicesapp.domain.models.ApprovalStatus
import com.example.titossycleaningservicesapp.domain.models.Gender
import org.threeten.bp.LocalDateTime
import java.util.UUID

data class Customer(
    val id: UUID,
    val username: String,
    val fullName: String,
    val phone: String,
    val profilePicture: String?,
    val address: List<Address> = emptyList(),
    val email: String,
    val password: String,
    val status: ApprovalStatus,
    val createdAt: LocalDateTime,
    val updatedAt: LocalDateTime
)

data class Address(
    val id: UUID,
    val customerId: UUID,
    val county: String,
    val region: String,
    val postalCode: String
)

data class CustomerState(
    val isLoading: Boolean = false,
    val customer: Customer? = null,
    val error: String? = null,
    val customers: List<Customer>? = emptyList()
)
