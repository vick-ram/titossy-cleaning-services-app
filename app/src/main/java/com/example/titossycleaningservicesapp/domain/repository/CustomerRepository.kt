package com.example.titossycleaningservicesapp.domain.repository

import com.example.titossycleaningservicesapp.core.Resource
import com.example.titossycleaningservicesapp.data.remote.util.AuthEvent
import com.example.titossycleaningservicesapp.domain.models.ui_models.Customer
import kotlinx.coroutines.flow.Flow
import java.util.UUID

interface CustomerRepository {
    suspend fun createCustomer(
        firstName: String,
        lastName: String,
        phone: String,
        email: String,
        password: String
    ): AuthEvent

    suspend fun signInCustomer(
        email: String,
        password: String
    ): AuthEvent
    suspend fun signOutCustomer(): AuthEvent
    suspend fun updateCustomer(
        customerId: String,
        firstName: String,
        lastName: String,
        phone: String,
        email: String,
        password: String
    ): AuthEvent


    fun getCustomers(): Flow<Resource<List<Customer>>>
    fun getCustomerById(id: UUID): Flow<Resource<Customer>>
    fun getCustomersByEmail(email: String): Flow<Resource<Customer>>

}