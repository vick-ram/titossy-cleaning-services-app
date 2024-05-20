package com.example.titossycleaningservicesapp.data.repository

import com.example.titossycleaningservicesapp.core.Resource
import com.example.titossycleaningservicesapp.data.local.datastore.DataStoreKeys
import com.example.titossycleaningservicesapp.data.remote.api.ApiService
import com.example.titossycleaningservicesapp.data.remote.dto.EmployeeDto
import com.example.titossycleaningservicesapp.data.remote.util.AuthEvent
import com.example.titossycleaningservicesapp.domain.models.ApprovalStatus
import com.example.titossycleaningservicesapp.domain.models.requests.employee.EmployeeSignInRequest
import com.example.titossycleaningservicesapp.domain.models.requests.employee.EmployeeStatusUpdate
import com.example.titossycleaningservicesapp.domain.repository.EmployeeRepository
import retrofit2.HttpException
import java.util.UUID
import javax.inject.Inject

class EmployeeRepositoryImpl @Inject constructor(
    private val dataStoreKeys: DataStoreKeys,
    private val apiService: ApiService
) : EmployeeRepository {
    override suspend fun signInEmployee(
        email: String,
        password: String
    ): AuthEvent {
        return try {
            val token = apiService.employeeSignIn(
                EmployeeSignInRequest(email, password)
            ).data
            token?.let { dataStoreKeys.saveTokenToDataStore(it) }
            AuthEvent.Success()
        }catch (e: Exception){
            when(e) {
                is HttpException -> {
                    when(e.code()) {
                        401 -> {
                            AuthEvent.Error("Invalid credentials")
                        }
                        else -> {
                            AuthEvent.Error("An error occurred")
                        }
                    }
                }

                else -> {
                    AuthEvent.Error("An error occurred")
                }
            }
        }
    }

    override suspend fun signOutEmployee(): AuthEvent {
        return try {
            val token = dataStoreKeys.getTokenFromDataStore()
            token?.let { apiService.employeeSignOut(it) }
            dataStoreKeys.clearToken()
            AuthEvent.Success()
        }catch (e: Exception) {
            AuthEvent.Error("An error occurred")
        }
    }

    override suspend fun updateAvailability(
        id: UUID,
        availability: EmployeeStatusUpdate
    ): AuthEvent {
        TODO("Not yet implemented")
    }

    override suspend fun getEmployeeById(id: UUID): Resource<EmployeeDto> {
        TODO("Not yet implemented")
    }

    override suspend fun getEmployeesByStatus(status: ApprovalStatus): Resource<List<EmployeeDto>> {
        TODO("Not yet implemented")
    }
}