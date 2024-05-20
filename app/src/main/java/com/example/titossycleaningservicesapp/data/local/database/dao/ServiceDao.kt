package com.example.titossycleaningservicesapp.data.local.database.dao

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Transaction
import androidx.room.Upsert
import com.example.titossycleaningservicesapp.data.local.database.entities.ServiceAddonEntity
import com.example.titossycleaningservicesapp.data.local.database.entities.ServiceEntity
import com.example.titossycleaningservicesapp.data.local.database.entities.ServiceWithAddOns
import kotlinx.coroutines.flow.Flow

@Dao
interface ServiceDao {
    @Transaction
    @Query("SELECT * FROM services")
    fun getAllServices(): Flow<List<ServiceWithAddOns>>

    @Transaction
    @Query("SELECT * FROM services WHERE LOWER(name) LIKE '%' || LOWER(:query) || '%'")
    fun searchServices(query: String): Flow<List<ServiceWithAddOns>>

    @Transaction
    @Upsert
    suspend fun insertAllServices(services: List<ServiceEntity>)

    @Transaction
    @Upsert
    suspend fun insertAllAddOns(addOns: List<ServiceAddonEntity>)

}