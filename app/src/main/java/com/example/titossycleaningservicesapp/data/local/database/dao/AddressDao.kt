package com.example.titossycleaningservicesapp.data.local.database.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Update
import androidx.room.Upsert
import com.example.titossycleaningservicesapp.data.local.database.entities.AddressEntity

@Dao
interface AddressDao {

    @Insert(entity = AddressEntity::class, onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAddresses(address: List<AddressEntity>)

    @Upsert
    suspend fun insertAddress(address: AddressEntity)

    @Update(entity = AddressEntity::class)
    suspend fun updateAddress(address: AddressEntity)

    @Delete
    suspend fun deleteAddress(address: AddressEntity)
}