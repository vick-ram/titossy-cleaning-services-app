package com.example.titossycleaningservicesapp.domain.repository

import com.example.titossycleaningservicesapp.core.Resource
import com.example.titossycleaningservicesapp.data.remote.util.AuthEvent
import com.example.titossycleaningservicesapp.domain.models.ui_models.Supplier
import java.util.UUID

interface SupplierRepository {
    //create a customer
    suspend fun createSupplier(
        username: String,
        firstName: String,
        lastName: String,
        phone: String,
        company: String,
        county: String,
        region: String,
        postalCode: String,
        email: String,
        password: String
    ): AuthEvent

    //sign in customer
    suspend fun signInSupplier(email: String, password: String): AuthEvent

    //sign out customer
    suspend fun signOutSupplier(): AuthEvent

    //update customer
    suspend fun updateSupplier(
        id: UUID,
        username: String,
        firstName: String,
        lastName: String,
        phone: String,
        company: String,
        county: String,
        region: String,
        postalCode: String,
        email: String,
        password: String
        ): AuthEvent

    //update customer status
    suspend fun getSupplierById(id: UUID): Resource<Supplier>
}