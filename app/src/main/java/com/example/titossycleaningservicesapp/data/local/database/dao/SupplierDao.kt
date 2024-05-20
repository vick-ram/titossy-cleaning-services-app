package com.example.titossycleaningservicesapp.data.local.database.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import com.example.titossycleaningservicesapp.data.local.database.entities.SupplierEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface SupplierDao {

    @Insert
    suspend fun insertSupplier(supplier: SupplierEntity)

    @Query("SELECT * FROM supplier")
    fun getAllSuppliers(): Flow<List<SupplierEntity>>

    @Query("SELECT * FROM supplier WHERE supplier_id = :id")
    fun getSupplierById(id: Int): Flow<SupplierEntity>

    @Update
    suspend fun updateSupplier(supplier: SupplierEntity)

    @Delete
    suspend fun deleteSupplier(supplier: SupplierEntity)
}