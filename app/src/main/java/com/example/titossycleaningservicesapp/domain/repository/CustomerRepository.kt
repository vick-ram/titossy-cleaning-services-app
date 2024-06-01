package com.example.titossycleaningservicesapp.domain.repository

import android.content.Context
import android.net.Uri
import com.example.titossycleaningservicesapp.core.Resource
import com.example.titossycleaningservicesapp.data.remote.util.AuthEvent
import com.example.titossycleaningservicesapp.domain.models.ui_models.Customer
import kotlinx.coroutines.flow.Flow
import java.util.UUID

interface CustomerRepository {
    suspend fun createCustomer(
        username: String,
        firstName: String,
        lastName: String,
        phone: String,
        email: String,
        password: String
    ): AuthEvent
    suspend fun signInCustomer(username: String? = null,email: String? = null, password: String): AuthEvent

    //sign out customer
    suspend fun signOutCustomer(): AuthEvent

    //update customer
    suspend fun updateCustomer(
        customerId: UUID,
        username: String,
        firstName: String,
        lastName: String,
        phone: String,
        email: String,
        password: String
    ): AuthEvent

    suspend fun updateCustomerProfilePicture(
        customerId: UUID,
        profilePicture: Uri,
        context: Context
    ): AuthEvent

    fun getCustomers(): Flow<Resource<List<Customer>>>

    //update customer status
    fun getCustomerById(id: UUID): Flow<Resource<Customer>>
    fun getCustomerByUsername(username: String) : Flow<Resource<Customer>>

    //get customer by email
    fun getCustomersByEmail(email: String): Flow<Resource<Customer>>

}