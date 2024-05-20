package com.example.titossycleaningservicesapp.data.local.database.entities

import androidx.room.ColumnInfo
import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.Relation
import com.example.titossycleaningservicesapp.domain.models.ApprovalStatus
import com.example.titossycleaningservicesapp.domain.models.ui_models.Address
import com.example.titossycleaningservicesapp.domain.models.ui_models.Customer
import org.threeten.bp.LocalDateTime
import java.util.UUID

@Entity("customers")
data class CustomerEntity(
    @ColumnInfo("customer_id") @PrimaryKey val id: UUID,
    @ColumnInfo("username") var username: String,
    @ColumnInfo("full_name") var fullName: String,
    @ColumnInfo("phone") var phone: String,
    @ColumnInfo("profile_picture") var profilePic: String? = null,
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
        profilePicture = profilePic,
        email = email,
        password = password,
        status = status,
        createdAt = createdAt,
        updatedAt = updatedAt
    )
}

@Entity("addresses")
data class AddressEntity(
    @ColumnInfo("address_id") @PrimaryKey val id: UUID,
    @ColumnInfo("customer_id") val customerId: UUID,
    @ColumnInfo("county") val county: String,
    @ColumnInfo("region") val region: String,
    @ColumnInfo("postal_code") val postalCode: String
) {
    fun toAddress() = Address(
        id = id,
        customerId = customerId,
        county = county,
        region = region,
        postalCode = postalCode
    )
}

data class CustomerWIthAddress(
    @Embedded val customer: CustomerEntity?,
    @Relation(
        parentColumn = "customer_id",
        entityColumn = "customer_id"
    )
    val address: List<AddressEntity> = emptyList()
) {
    fun toCustomerWithAddress() = customer?.let { cust ->
        Customer(
            id = cust.id,
            username = cust.username,
            fullName = cust.fullName,
            phone = cust.phone,
            profilePicture = cust.profilePic,
            address = address.map { it.toAddress() },
            email = cust.email,
            password = cust.password,
            status = cust.status,
            createdAt = cust.createdAt,
            updatedAt = cust.updatedAt
        )
    }
}


