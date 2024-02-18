package com.example.titossycleaningservicesapp.data.repo

import com.example.titossycleaningservicesapp.data.database.user.data.Employees
import com.example.titossycleaningservicesapp.data.utils.Resource

import kotlinx.coroutines.flow.Flow

interface EmployeeRepository {

    suspend fun getAllEmployeeData(): Flow<Resource<List<Employees>>>
    suspend fun fetchEmployee(): Flow<Resource<Employees>>
}