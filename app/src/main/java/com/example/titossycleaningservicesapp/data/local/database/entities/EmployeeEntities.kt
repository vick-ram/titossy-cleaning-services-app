package com.example.titossycleaningservicesapp.data.local.database.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.titossycleaningservicesapp.domain.models.ApprovalStatus
import com.example.titossycleaningservicesapp.domain.models.Availability
import com.example.titossycleaningservicesapp.domain.models.Gender
import com.example.titossycleaningservicesapp.domain.models.Roles
import com.example.titossycleaningservicesapp.domain.models.ui_models.Employee
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.util.UUID

@Entity("employee")
data class EmployeeEntity(
    @ColumnInfo("employee_id") @PrimaryKey val id: String,
    @ColumnInfo("username") val username: String,
    @ColumnInfo("full_name") val fullName: String,
    @ColumnInfo("gender") val gender: String,
    @ColumnInfo("email") val email: String,
    @ColumnInfo("password") val password: String,
    @ColumnInfo("phone") val phone: String,
    @ColumnInfo("role") val role: String,
    @ColumnInfo("status") val availability: String,
    @ColumnInfo("approval_status") val approvalStatus: String,
    @ColumnInfo("created_at") val createdAt: String,
    @ColumnInfo("updated_at") val updatedAt: String
) {
    fun toEmployee() = Employee(
        id = id,
        username = username,
        fullName = fullName,
        phone = phone,
        gender = Gender.valueOf(gender),
        email = email,
        password = password,
        availability = Availability.valueOf(availability),
        approvalStatus = ApprovalStatus.valueOf(approvalStatus),
        role = Roles.valueOf(role),
        createdAt = LocalDateTime.parse(createdAt, DateTimeFormatter.ISO_LOCAL_DATE_TIME),
        updatedAt = LocalDateTime.parse(updatedAt, DateTimeFormatter.ISO_LOCAL_DATE_TIME)
    )
}