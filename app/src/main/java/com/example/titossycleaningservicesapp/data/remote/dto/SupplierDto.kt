package com.example.titossycleaningservicesapp.data.remote.dto

import com.example.titossycleaningservicesapp.data.local.database.entities.SupplierEntity
import com.example.titossycleaningservicesapp.domain.models.ApprovalStatus
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.util.UUID

data class SupplierDto(
    val id: String,
    val fullName: String,
    val phone: String,
    val address: String,
    val email: String,
    val password: String,
    val status: String,
    val createdAt: String,
    val updatedAt: String
) {
    fun toSupplierEntity() = SupplierEntity(
            id = id,
            fullName = fullName,
            phone = phone,
            address = address,
            email = email,
            password = password,
            status = ApprovalStatus.valueOf(status),
            createdAt = LocalDateTime.parse(updatedAt, DateTimeFormatter.ISO_LOCAL_DATE_TIME),
            updatedAt = LocalDateTime.parse(updatedAt, DateTimeFormatter.ISO_LOCAL_DATE_TIME)
        )
}
