package com.example.titossycleaningservicesapp.data.local.database.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import androidx.room.Upsert
import com.example.titossycleaningservicesapp.data.local.database.entities.SupplierEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface SupplierDao {

    @Transaction
    @Insert(entity = SupplierEntity::class, onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertSupplier(supplier: SupplierEntity)

    @Transaction
    @Insert(entity = SupplierEntity::class, OnConflictStrategy.REPLACE)
    fun insertAllSuppliers(suppliers: List<SupplierEntity>)

    @Transaction
    @Query("SELECT * FROM suppliers")
    fun getAllSuppliers(): Flow<List<SupplierEntity>>

    @Transaction
    @Query("SELECT * FROM suppliers WHERE supplier_id = :id")
    fun getSupplierById(id: String): Flow<SupplierEntity>

    @Transaction
    @Query("SELECT * FROM suppliers WHERE email = :email")
    fun getSupplierByEmail(email: String): Flow<SupplierEntity>

    @Transaction
    @Upsert
    suspend fun updateSupplier(supplier: SupplierEntity)

    @Transaction
    @Query("UPDATE suppliers SET status = :status WHERE supplier_id = :id")
    suspend fun updateSupplierStatus(id: String, status: String)

    @Transaction
    @Delete
    suspend fun deleteSupplier(supplier: SupplierEntity)

    @Transaction
    @Query("DELETE FROM suppliers")
    suspend fun deleteAllSuppliers()
}