package com.example.titossycleaningservicesapp.domain.repository

import com.example.titossycleaningservicesapp.core.Resource
import com.example.titossycleaningservicesapp.data.remote.util.AuthEvent
import com.example.titossycleaningservicesapp.domain.models.ApprovalStatus
import com.example.titossycleaningservicesapp.domain.models.ui_models.ChatMessage
import com.example.titossycleaningservicesapp.domain.models.ui_models.Supplier
import kotlinx.coroutines.flow.Flow
import java.util.UUID

interface SupplierRepository {
    suspend fun createSupplier(
        firstName: String,
        lastName: String,
        phone: String,
        address: String,
        email: String,
        password: String
    ): AuthEvent

    suspend fun signInSupplier(email: String, password: String): AuthEvent
    suspend fun signOutSupplier(): AuthEvent
    suspend fun updateSupplier(
        id: String,
        firstName: String,
        lastName: String,
        phone: String,
        address: String,
        email: String,
        password: String
    ): AuthEvent

    suspend fun updateSupplierStatus(
        id: String,
        approvalStatus: ApprovalStatus
    ): AuthEvent

    suspend fun deleteSupplier(id: String) : AuthEvent

    fun getAllSuppliers(): Flow<Resource<List<Supplier>>>

    suspend fun getSupplierById(id: String): Flow<Resource<Supplier>>
    fun getSupplierByEmail(email: String): Flow<Resource<Supplier>>

    fun getSupplierMessages(receiver: String): Flow<Resource<List<ChatMessage>>>
}