package com.example.titossycleaningservicesapp.domain.models.ui_models

import com.example.titossycleaningservicesapp.domain.models.ApprovalStatus
import java.time.LocalDateTime

data class Supplier(
    val id: String,
    val fullName: String,
    val phone: String,
    val address: String,
    val email: String,
    val password: String,
    val status: ApprovalStatus,
    val createdAt: LocalDateTime,
    val updatedAt: LocalDateTime
)

data class SupplierUiState(
    val isLoading: Boolean = false,
    val errorMessage: String = "",
    val suppliers: List<Supplier>? = null,
    val supplier: Supplier? = null
)

