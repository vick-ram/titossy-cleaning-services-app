package com.example.titossycleaningservicesapp.domain.models.ui_models

import com.example.titossycleaningservicesapp.domain.models.Availability
import com.example.titossycleaningservicesapp.domain.models.Gender
import com.example.titossycleaningservicesapp.domain.models.Roles
import org.threeten.bp.LocalDateTime
import java.util.UUID

data class Employee(
    val id: UUID,
    val username: String,
    val firstName: String,
    val lastName: String,
    val phone: String,
    val gender: Gender,
    val email: String,
    val password: String,
    val availability: Availability,
    val role: Roles,
    val createdAt: LocalDateTime,
    val updatedAt: LocalDateTime
)