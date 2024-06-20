package com.example.titossycleaningservicesapp.domain.models.ui_models

import com.example.titossycleaningservicesapp.domain.models.ApprovalStatus
import com.example.titossycleaningservicesapp.domain.models.Availability
import com.example.titossycleaningservicesapp.domain.models.Gender
import com.example.titossycleaningservicesapp.domain.models.Roles
import java.time.LocalDateTime
import java.util.UUID

data class Employee(
    val id: UUID,
    val username: String,
    val fullName: String,
    val gender: Gender,
    val email: String,
    val password: String,
    val phone: String,
    val role: Roles,
    val availability: Availability,
    val approvalStatus: ApprovalStatus,
    val createdAt: LocalDateTime,
    val updatedAt: LocalDateTime
)