package com.example.titossycleaningservicesapp.data.local.database.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.titossycleaningservicesapp.domain.models.ApprovalStatus
import com.example.titossycleaningservicesapp.domain.models.ui_models.Supplier
import java.time.LocalDateTime
import java.util.UUID

@Entity("suppliers")
data class SupplierEntity(
    @ColumnInfo("supplier_id") @PrimaryKey val id: UUID,
    @ColumnInfo("full_name") val fullName: String,
    @ColumnInfo("phone") val phone: String,
    @ColumnInfo("address") val address: String,
    @ColumnInfo("email") val email: String,
    @ColumnInfo("password") val password: String,
    @ColumnInfo("status") val status: ApprovalStatus,
    @ColumnInfo("created_at") val createdAt: LocalDateTime,
    @ColumnInfo("updated_at") val updatedAt: LocalDateTime,
) {
    fun toSupplier(): Supplier {
        return Supplier(
            id = id,
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
}
