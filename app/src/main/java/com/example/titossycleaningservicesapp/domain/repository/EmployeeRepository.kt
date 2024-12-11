package com.example.titossycleaningservicesapp.domain.repository

import com.example.titossycleaningservicesapp.core.Resource
import com.example.titossycleaningservicesapp.data.remote.dto.EmployeeDto
import com.example.titossycleaningservicesapp.data.remote.util.AuthEvent
import com.example.titossycleaningservicesapp.domain.models.ApprovalStatus
import com.example.titossycleaningservicesapp.domain.models.requests.employee.EmployeeStatusUpdate
import com.example.titossycleaningservicesapp.domain.models.ui_models.Employee
import kotlinx.coroutines.flow.Flow
import java.util.UUID

interface EmployeeRepository {
    suspend fun signInEmployee(email: String, password: String): AuthEvent
    suspend fun signOutEmployee(): AuthEvent
    suspend fun updateAvailability(id: String, availability: String): AuthEvent
    fun getAllEmployees() : Flow<Resource<List<Employee>>>
    fun getEmployeesByRole(role: String) : Flow<Resource<List<Employee>>>
    fun getEmployeeById(id: String): Flow<Resource<Employee>>
    fun getEmployeesByStatus(status: String): Flow<Resource<List<Employee>>>
    fun getEmployeeByEmail(email: String): Flow<Resource<Employee>>
}