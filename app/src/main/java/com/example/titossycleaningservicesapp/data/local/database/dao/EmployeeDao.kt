package com.example.titossycleaningservicesapp.data.local.database.dao

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Update
import androidx.room.Upsert
import com.example.titossycleaningservicesapp.data.local.database.entities.EmployeeEntity
import com.example.titossycleaningservicesapp.domain.models.ApprovalStatus
import kotlinx.coroutines.flow.Flow
import java.util.UUID

@Dao
interface EmployeeDao {
    @Query("SELECT * FROM employee WHERE status = :status")
    fun getEmployeesByStatus(status: String): Flow<List<EmployeeEntity>>

    @Query("SELECT * FROM employee WHERE employee_id = :id")
    fun getEmployeeById(id: UUID): Flow<EmployeeEntity>

    @Upsert
    suspend fun insertEmployees(employees: List<EmployeeEntity>)
    @Upsert
    suspend fun insertEmployee(employee: EmployeeEntity)
    @Query("UPDATE employee SET status = :status WHERE employee_id = :id")
    suspend fun updateEmployeeStatus(status: String, id: UUID)
}