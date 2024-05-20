package com.example.titossycleaningservicesapp.data.local.database.entities

import androidx.room.ColumnInfo
import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.titossycleaningservicesapp.data.remote.dto.dateTimeFormatter
import com.example.titossycleaningservicesapp.domain.models.ApprovalStatus
import com.example.titossycleaningservicesapp.domain.models.ui_models.Supplier
import com.example.titossycleaningservicesapp.domain.models.ui_models.SupplierAddress
import org.threeten.bp.LocalDateTime
import java.util.UUID

@Entity("supplier")
data class SupplierEntity(
    @ColumnInfo("supplier_id") @PrimaryKey val id: String,
    @ColumnInfo("username") val username: String,
    @ColumnInfo("full_name") val fullName: String,
    @ColumnInfo("phone") val phone: String,
    @ColumnInfo("company") val company: String,
    @Embedded val address: SupplierAddressEntity?,
    @ColumnInfo("email") val email: String,
    @ColumnInfo("password") val password: String,
    @ColumnInfo("status") val status: String,
    @ColumnInfo("created_at") val createdAt: String,
    @ColumnInfo("updated_at") val updatedAt: String,
) {
    fun toSupplier() = address?.let {
        Supplier(
            id = UUID.fromString(id),
            username = username,
            fullName = fullName,
            phone = phone,
            company = company,
            address = it.toSupplierAddress(),
            email = email,
            password = password,
            status = ApprovalStatus.valueOf(status),
            createdAt = LocalDateTime.parse(createdAt, dateTimeFormatter),
            updatedAt = LocalDateTime.parse(updatedAt, dateTimeFormatter)
        )
    }
}

data class SupplierAddressEntity(
    val county: String?,
    val region: String?,
    val postalCode: String?
) {
    fun toSupplierAddress() = SupplierAddress(
        county = county!!,
        region = region!!,
        postalCode = postalCode!!
    )
}