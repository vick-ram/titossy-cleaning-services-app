package com.example.titossycleaningservicesapp.data.remote.dto

import com.example.titossycleaningservicesapp.data.local.database.entities.EmployeeEntity
import com.example.titossycleaningservicesapp.domain.models.ApprovalStatus
import com.example.titossycleaningservicesapp.domain.models.Availability
import com.example.titossycleaningservicesapp.domain.models.Gender
import com.example.titossycleaningservicesapp.domain.models.Roles
import com.example.titossycleaningservicesapp.domain.models.ui_models.Employee
import java.time.LocalDateTime
import java.util.UUID

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

    fun toEmployeeModel() = Employee(
        id = UUID.fromString(id),
        username = username,
        fullName = fullName,
        phone = phone,
        gender = Gender.valueOf(gender),
        email = email,
        password = password,
        availability = Availability.valueOf( availability),
        approvalStatus = ApprovalStatus.valueOf(approvalStatus),
        role = Roles.valueOf(role),
        createdAt = LocalDateTime.parse(createdAt),
        updatedAt = LocalDateTime.parse(updatedAt)
    )
}