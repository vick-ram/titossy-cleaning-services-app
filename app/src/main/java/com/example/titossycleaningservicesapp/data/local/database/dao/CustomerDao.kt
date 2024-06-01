package com.example.titossycleaningservicesapp.data.local.database.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import com.example.titossycleaningservicesapp.data.local.database.entities.CustomerEntity
import com.example.titossycleaningservicesapp.data.local.database.entities.CustomerWIthAddress
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
        username = :username,
        full_name = :fullName,
        phone = :phone,
        email = :email,
        password = :password
    WHERE customer_id = :id
"""
    )
    suspend fun updateCustomer(
        id: UUID,
        username: String,
        fullName: String,
        phone: String,
        email: String,
        password: String
    )

    @Query("UPDATE customers SET profile_picture = :profilePicture WHERE customer_id = :id")
    suspend fun updateCustomerProfilePicture(id: UUID, profilePicture: String)

    @Delete
    suspend fun deleteCustomer(customer: CustomerEntity)

    @Transaction
    @Query("SELECT * FROM customers WHERE username = :username")
    suspend fun getCustomerByUsername(username: String): CustomerWIthAddress?

    @Transaction
    @Query("SELECT * FROM customers")
    fun getCustomers() : Flow<List<CustomerWIthAddress>>

    @Transaction
    @Query("SELECT * FROM customers WHERE email = :email")
    suspend fun getCustomerByEmail(email: String): CustomerWIthAddress?

    @Transaction
    @Query("SELECT * FROM customers WHERE customer_id = :id")
    suspend fun getCustomerById(id: UUID): CustomerWIthAddress?

    @Transaction
    @Query("SELECT * FROM customers WHERE email = :input OR username = :input")
    suspend fun getCustomerByUsernameOrEmail(input: String) : CustomerWIthAddress?


}