package com.example.titossycleaningservicesapp.domain.repository

import com.example.titossycleaningservicesapp.data.remote.util.AuthEvent
import com.example.titossycleaningservicesapp.domain.models.requests.customer.CustomerAddressRequest
import java.util.UUID

interface AddressRepository {

    suspend fun insertCustomerAddress(customerId: UUID, address: CustomerAddressRequest): AuthEvent
    suspend fun deleteCustomerAddress(customerId: UUID, addressId: UUID): AuthEvent
    suspend fun updateCustomerAddress(customerId: UUID, addressId: UUID, address: CustomerAddressRequest): AuthEvent
}