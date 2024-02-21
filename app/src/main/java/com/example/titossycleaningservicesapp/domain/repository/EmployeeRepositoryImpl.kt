package com.example.titossycleaningservicesapp.domain.repository

import android.util.Log
import com.example.titossycleaningservicesapp.data.database.user.data.Employees
import com.example.titossycleaningservicesapp.data.repo.EmployeeRepository
import com.example.titossycleaningservicesapp.data.utils.Resource
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

class EmployeeRepositoryImpl @Inject constructor(
    private val fireStore: FirebaseFirestore,
    private val firebaseAuth: FirebaseAuth
) : EmployeeRepository {
    private val currentUser = firebaseAuth.currentUser
    override suspend fun getAllEmployeeData(): Flow<Resource<List<Employees>>> {
        return flow {
            emit(Resource.Loading())
            val docRef = fireStore.collection("employees").get().await()
            val employee = docRef.documents.mapNotNull { it.toObject(Employees::class.java) }
            emit(Resource.Success(employee))
            Log.d("USER DATA", "${employee.toHashSet()}")
        }.catch { e ->
            emit(Resource.Failure(e.toString()))
        }
    }


    override suspend fun fetchEmployee(): Flow<Resource<Employees>> {
        return flow {
            emit(Resource.Loading())
            if (currentUser != null) {
                val docRef =
                    fireStore.collection("employees").document(currentUser.uid).get().await()
                docRef.toObject(Employees::class.java)?.let { employee ->
                    emit(Resource.Success(employee))
                } ?: emit(Resource.Failure(("Employee not found")))
            }
        }.catch {
            emit(Resource.Failure(it.toString()))
        }.flowOn(Dispatchers.IO)
    }
}