package com.example.titossycleaningservicesapp.data.local.database.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.titossycleaningservicesapp.domain.models.ApprovalStatus
import com.example.titossycleaningservicesapp.domain.models.ui_models.Customer
import java.time.LocalDateTime
import java.util.UUID

@Entity("customers")
data class CustomerEntity(
    @ColumnInfo("customer_id") @PrimaryKey val id: UUID,
    @ColumnInfo("username") var username: String,
    @ColumnInfo("full_name") var fullName: String,
    @ColumnInfo("phone") var phone: String,
    @ColumnInfo("address") var address: String?,
    @ColumnInfo("email") var email: String,
    @ColumnInfo("password") var password: String,
    @ColumnInfo("approval_status") var status: ApprovalStatus,
    @ColumnInfo("created_at") var createdAt: LocalDateTime,
    @ColumnInfo("updated_at") var updatedAt: LocalDateTime
) {
    fun toCustomer() = Customer(
        id = id,
        username = username,
        fullName = fullName,
        phone = phone,
        address = address,
        email = email,
        password = password,
        status = status,
        createdAt = createdAt,
        updatedAt = updatedAt
    )
}



