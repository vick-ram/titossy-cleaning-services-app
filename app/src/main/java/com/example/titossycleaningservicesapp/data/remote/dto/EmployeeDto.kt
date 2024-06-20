package com.example.titossycleaningservicesapp.data.remote.dto

import com.example.titossycleaningservicesapp.data.local.database.entities.EmployeeEntity

data class EmployeeDto(
    val id: String,
    val username: String,
    val fullName: String,
    val gender: String,
    val email: String,
    val password: String,
    val phone: String,
    val role: String,
    val availability: String,
    val approvalStatus: String,
    val createdAt: String,
    val updatedAt: String
) {
    fun toEmployee() = EmployeeEntity(
        id = id,
        username = username,
        fullName = fullName,
        phone = phone,
        gender = gender,
        email = email,
        password = password,
        availability = availability,
        approvalStatus = approvalStatus,
        role = role,
        createdAt = createdAt,
        updatedAt = updatedAt
    )
}