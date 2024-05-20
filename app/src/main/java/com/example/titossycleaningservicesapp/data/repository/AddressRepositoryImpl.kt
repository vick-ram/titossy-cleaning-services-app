package com.example.titossycleaningservicesapp.data.repository

import com.example.titossycleaningservicesapp.data.remote.util.AuthEvent
import com.example.titossycleaningservicesapp.domain.models.requests.customer.CustomerAddressRequest
import com.example.titossycleaningservicesapp.domain.repository.AddressRepository
import java.util.UUID

class AddressRepositoryImpl : AddressRepository {
    override suspend fun insertCustomerAddress(
        customerId: UUID,
        address: CustomerAddressRequest
    ): AuthEvent {
        TODO("Not yet implemented")
    }

    override suspend fun deleteCustomerAddress(
        customerId: UUID,
        addressId: UUID
    ): AuthEvent {
        TODO("Not yet implemented")
    }

    override suspend fun updateCustomerAddress(
        customerId: UUID,
        addressId: UUID,
        address: CustomerAddressRequest
    ): AuthEvent {
        TODO("Not yet implemented")
    }
}