package com.example.titossycleaningservicesapp.data.local.database.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import com.example.titossycleaningservicesapp.data.local.database.entities.CustomerEntity
import kotlinx.coroutines.flow.Flow
import java.util.UUID

@Dao
interface CustomerDao {

    @Transaction
    @Insert(entity = CustomerEntity::class, onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertCustomer(customer: CustomerEntity)

    @Transaction
    @Insert
    suspend fun insertCustomers(customersWithAddress: List<CustomerEntity>)

    @Query(
        """
    UPDATE customers SET
        full_name = :fullName,
        phone = :phone,
        email = :email,
        password = :password
    WHERE customer_id = :id
"""
    )
    suspend fun updateCustomer(
        id: String,
        fullName: String,
        phone: String,
        email: String,
        password: String
    )

    @Delete
    suspend fun deleteCustomer(customer: CustomerEntity)

    @Transaction
    @Query("SELECT * FROM customers")
    fun getCustomers() : Flow<List<CustomerEntity>>

    @Transaction
    @Query("SELECT * FROM customers WHERE email = :email")
    suspend fun getCustomerByEmail(email: String): CustomerEntity?

    @Transaction
    @Query("SELECT * FROM customers WHERE customer_id = :id")
    suspend fun getCustomerById(id: UUID): CustomerEntity?

    @Transaction
    @Query("DELETE FROM customers")
    suspend fun deleteAllCustomers()


}