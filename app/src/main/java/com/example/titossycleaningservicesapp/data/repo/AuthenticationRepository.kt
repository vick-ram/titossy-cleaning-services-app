package com.example.titossycleaningservicesapp.data.repo

import com.example.titossycleaningservicesapp.data.database.user.data.Customer
import com.example.titossycleaningservicesapp.data.utils.Resource
import com.example.titossycleaningservicesapp.data.database.user.data.Employees
import com.google.firebase.auth.FirebaseUser
import kotlinx.coroutines.flow.Flow

interface AuthenticationRepository {
    val currentUser: FirebaseUser?
    suspend fun loginUser(email: String, password: String): Flow<Resource<Customer>>
    suspend fun registerUser(
        userId: String,
        firstName: String,
        lastName: String,
        email: String,
        password: String
    ): Flow<Resource<Customer>>

    suspend fun signOut(): Flow<Resource<Boolean>>

}
