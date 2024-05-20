package com.example.titossycleaningservicesapp.data.remote.dto

import com.example.titossycleaningservicesapp.data.local.database.entities.EmployeeEntity
import com.example.titossycleaningservicesapp.domain.models.Availability
import com.example.titossycleaningservicesapp.domain.models.Gender
import com.example.titossycleaningservicesapp.domain.models.Roles
import org.threeten.bp.LocalDateTime
import java.util.UUID

data class EmployeeDto(
    val id: String,
    val username: String,
    val firstName: String,
    val lastName: String,
    val phone: String,
    val gender: String,
    val email: String,
    val password: String,
    val availability: String,
    val role: String,
    val createdAt: String,
    val updatedAt: String
) {
    fun toEmployee() = EmployeeEntity(
        id = id,
        username = username,
        firstName = firstName,
        lastName = lastName,
        phone = phone,
        gender = gender,
        email = email,
        password = password,
        availability = availability,
        role = role,
        createdAt = createdAt,
        updatedAt = updatedAt
    )
}