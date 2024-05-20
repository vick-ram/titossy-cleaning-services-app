package com.example.titossycleaningservicesapp.domain.models.ui_models

import com.example.titossycleaningservicesapp.domain.models.ApprovalStatus
import org.threeten.bp.LocalDateTime
import java.util.UUID

data class Supplier(
    val id: UUID,
    val username: String,
    val fullName: String,
    val phone: String,
    val company: String,
    val address: SupplierAddress,
    val email: String,
    val password: String,
    val status: ApprovalStatus,
    val createdAt: LocalDateTime,
    val updatedAt: LocalDateTime
)

data class SupplierAddress(
    val county: String,
    val region: String,
    val postalCode: String
)