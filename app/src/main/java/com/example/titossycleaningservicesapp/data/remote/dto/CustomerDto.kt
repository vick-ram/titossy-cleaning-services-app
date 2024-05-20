package com.example.titossycleaningservicesapp.data.remote.dto

import com.example.titossycleaningservicesapp.data.local.database.entities.AddressEntity
import com.example.titossycleaningservicesapp.data.local.database.entities.CustomerEntity
import com.example.titossycleaningservicesapp.data.local.database.entities.CustomerWIthAddress
import com.example.titossycleaningservicesapp.domain.models.ApprovalStatus
import org.threeten.bp.LocalDateTime
import java.util.UUID

/*data class CustomerDto(
    val success: String,
    val statusCode: Int,
    val data: List<CustomerData>,
    val message: String?
)*/

data class CustomerDto(
    val id: String,
    val username: String,
    val fullName: String,
    val phone: String,
    val profilePicture: String?,
    val address: List<AddressDto> = emptyList(),
    val email: String,
    val password: String,
    val status: String,
    val createdAt: String,
    val updatedAt: String
) {
    fun toCustomerEntity() = CustomerWIthAddress(
        customer = CustomerEntity(
            id = UUID.fromString(id),
            username = username,
            fullName = fullName,
            phone = phone,
            profilePic = profilePicture,
            email = email,
            password = password,
            status = ApprovalStatus.valueOf(status),
            createdAt = LocalDateTime.parse(createdAt),
            updatedAt = LocalDateTime.parse(updatedAt)
        ),
        address = address.map { it.toAddressEntity() }
    )
}

data class AddressDto(
    val id: String,
    val customerId: String,
    val county: String,
    val region: String,
    val postalCode: String
) {
    fun toAddressEntity() = AddressEntity(
        id = UUID.fromString(id),
        customerId = UUID.fromString(customerId),
        county = county,
        region = region,
        postalCode = postalCode
    )
}




