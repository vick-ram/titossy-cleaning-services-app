package com.example.titossycleaningservicesapp.data.local.database.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.titossycleaningservicesapp.data.remote.dto.dateTimeFormatter
import com.example.titossycleaningservicesapp.domain.models.Availability
import com.example.titossycleaningservicesapp.domain.models.Gender
import com.example.titossycleaningservicesapp.domain.models.Roles
import com.example.titossycleaningservicesapp.domain.models.ui_models.Employee
import org.threeten.bp.LocalDateTime
import java.util.UUID

@Entity("employee")
data class EmployeeEntity(
    @ColumnInfo("employee_id") @PrimaryKey val id: String,
    @ColumnInfo("username") val username: String,
    @ColumnInfo("first_name") val firstName: String,
    @ColumnInfo("last_name") val lastName: String,
    @ColumnInfo("phone") val phone: String,
    @ColumnInfo("gender") val gender: String,
    @ColumnInfo("email") val email: String,
    @ColumnInfo("password") val password: String,
    @ColumnInfo("status") val availability: String,
    @ColumnInfo("role") val role: String,
    @ColumnInfo("created_at") val createdAt: String,
    @ColumnInfo("updated_at") val updatedAt: String
) {
    fun toEmployee() = Employee(
        id = UUID.fromString(id),
        username = username,
        firstName = firstName,
        lastName = lastName,
        phone = phone,
        gender = Gender.valueOf(gender),
        email = email,
        password = password,
        availability = Availability.valueOf(availability),
        role = Roles.valueOf(role),
        createdAt = LocalDateTime.parse(createdAt, dateTimeFormatter),
        updatedAt = LocalDateTime.parse(updatedAt, dateTimeFormatter)
    )
}