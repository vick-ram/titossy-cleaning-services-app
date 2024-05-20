package com.example.titossycleaningservicesapp.domain.repository

import com.example.titossycleaningservicesapp.core.Resource
import com.example.titossycleaningservicesapp.data.remote.dto.EmployeeDto
import com.example.titossycleaningservicesapp.data.remote.util.AuthEvent
import com.example.titossycleaningservicesapp.domain.models.ApprovalStatus
import com.example.titossycleaningservicesapp.domain.models.requests.employee.EmployeeStatusUpdate
import java.util.UUID

interface EmployeeRepository {
    suspend fun signInEmployee(email: String, password: String): AuthEvent
    //sign out employee
    suspend fun signOutEmployee(): AuthEvent
    //update employee status
    suspend fun updateAvailability(id: UUID, availability: EmployeeStatusUpdate): AuthEvent
    suspend fun getEmployeeById(id: UUID): Resource<EmployeeDto>
    suspend fun getEmployeesByStatus(status: ApprovalStatus): Resource<List<EmployeeDto>>
}