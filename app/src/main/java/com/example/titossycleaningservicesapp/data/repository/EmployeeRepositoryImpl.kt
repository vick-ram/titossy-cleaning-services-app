package com.example.titossycleaningservicesapp.data.repository

import com.example.titossycleaningservicesapp.core.FileUtils
import com.example.titossycleaningservicesapp.core.Resource
import com.example.titossycleaningservicesapp.data.local.database.dao.EmployeeDao
import com.example.titossycleaningservicesapp.data.local.datastore.DataStoreKeys
import com.example.titossycleaningservicesapp.data.remote.api.ApiService
import com.example.titossycleaningservicesapp.data.remote.dto.EmployeeDto
import com.example.titossycleaningservicesapp.data.remote.util.AuthEvent
import com.example.titossycleaningservicesapp.domain.models.ApprovalStatus
import com.example.titossycleaningservicesapp.domain.models.Availability
import com.example.titossycleaningservicesapp.domain.models.requests.employee.EmployeeAvailability
import com.example.titossycleaningservicesapp.domain.models.requests.employee.EmployeeSignInRequest
import com.example.titossycleaningservicesapp.domain.models.requests.employee.EmployeeStatusUpdate
import com.example.titossycleaningservicesapp.domain.models.ui_models.Employee
import com.example.titossycleaningservicesapp.domain.repository.EmployeeRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.flow
import retrofit2.HttpException
import java.util.UUID
import javax.inject.Inject

class EmployeeRepositoryImpl @Inject constructor(
    private val dataStoreKeys: DataStoreKeys,
    private val apiService: ApiService,
    private val employeeDao: EmployeeDao
) : EmployeeRepository {
    override suspend fun signInEmployee(
        email: String,
        password: String
    ): AuthEvent {
        return try {
            val apiResponse = apiService.employeeSignIn(EmployeeSignInRequest(email, password))

            when (apiResponse.status) {
                "success" -> {
                    apiResponse.data?.let { dataStoreKeys.saveTokenToDataStore(it) }
                    AuthEvent.Success()
                }

                "error" -> {
                    if (apiResponse.error != null) {
                        val error = FileUtils.createErrorMessage(apiResponse.error)
                        throw Exception(error)
                    } else {
                        throw Exception("Unknown error occurred during sign in")
                    }
                }

                else -> throw Exception("Unknown: status ${apiResponse.statusCode}")
            }
        } catch (e: Exception) {
            AuthEvent.Error(e.message.toString())
        }
    }

    override suspend fun signOutEmployee(): AuthEvent {
        return try {
            val token = dataStoreKeys.getTokenFromDataStore()
            val apiResponse = token?.let { apiService.employeeSignOut("Bearer $it") }

            when (apiResponse?.status) {
                "success" -> {
                    dataStoreKeys.clearToken()
                    AuthEvent.Success()
                }

                "error" -> {
                    if (apiResponse.error != null) {
                        val error = FileUtils.createErrorMessage(apiResponse.error)
                        throw Exception(error)
                    } else {
                        throw Exception("Unknown error occurred")
                    }
                }

                else -> throw Exception("Something went wrong with status code: ${apiResponse?.statusCode}")
            }
        } catch (e: Exception) {
            AuthEvent.Error(e.message.toString())
        }
    }

    override suspend fun updateAvailability(
        id: UUID,
        availability: String
    ): AuthEvent {
        return try {
            val apResponse = apiService.updateEmployeeAvailability(
                EmployeeAvailability(
                    Availability.valueOf(availability)
                )
            )
            when (apResponse.status) {
                "success" -> {
                    employeeDao.updateEmployeeStatus(availability, id)
                    AuthEvent.Success()
                }

                "error" -> {
                    if (apResponse.error != null) {
                        val errorMessage = FileUtils.createErrorMessage(apResponse.error)
                        throw Exception(errorMessage)
                    } else {
                        throw Exception("Something went wrong")
                    }
                }
                else -> throw Exception("Error: ${apResponse.statusCode}")
            }
        } catch (e: Exception) {
            AuthEvent.Error(e.message.toString())
        }
    }

    override suspend fun getEmployeeById(id: UUID): Flow<Resource<Employee>> {
        return flow {
            emit(Resource.Loading)
            val dbEmployee = employeeDao.getEmployeeById(id).firstOrNull()

            if (dbEmployee == null) {
                val apiEmployee = apiService.getEmployeeById(id.toString())
                when (apiEmployee.status) {
                    "success" -> {
                        val entityEmployee = apiEmployee.data?.toEmployee()
                        entityEmployee?.let { employeeDao.insertEmployee(it) }
                    }

                    "error" -> {
                        if (apiEmployee.error != null) {
                            val errorMessage = FileUtils.createErrorMessage(apiEmployee.error)
                            throw Exception(errorMessage)
                        }
                    }
                }
            }
            val employee = dbEmployee?.toEmployee()
            employee?.let { emit(Resource.Success(it)) }
        }.catch { e ->
            emit(Resource.Error(e.message.toString()))
        }
    }

    override suspend fun getEmployeesByStatus(status: String): Flow<Resource<List<Employee>>> {
        return flow {
            emit(Resource.Loading)
            val dbEmployees = employeeDao.getEmployeesByStatus(status).firstOrNull()

            if (dbEmployees == null) {
                val apiResponse = apiService.getAvailableEmployees(status)
                when (apiResponse.status) {
                    "success" -> {
                        val entityEmp = apiResponse.data?.map { it.toEmployee() }
                        entityEmp?.let { employeeDao.insertEmployees(it) }
                    }

                    "error" -> {
                        if (apiResponse.error != null) {
                            val errorMessage = FileUtils.createErrorMessage(apiResponse.error)
                            throw Exception(errorMessage)
                        }
                    }
                }
            }
            val employees = dbEmployees?.map { it.toEmployee() }
            employees?.let { emit(Resource.Success(it)) }

        }.catch { e ->
            emit(Resource.Error(e.message.toString()))
        }
    }
}