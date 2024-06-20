package com.example.titossycleaningservicesapp.data.local.database.db

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.example.titossycleaningservicesapp.data.local.database.dao.CustomerDao
import com.example.titossycleaningservicesapp.data.local.database.dao.EmployeeDao
import com.example.titossycleaningservicesapp.data.local.database.dao.ServiceDao
import com.example.titossycleaningservicesapp.data.local.database.dao.SupplierDao
import com.example.titossycleaningservicesapp.data.local.database.entities.Converters
import com.example.titossycleaningservicesapp.data.local.database.entities.CustomerEntity
import com.example.titossycleaningservicesapp.data.local.database.entities.EmployeeEntity
import com.example.titossycleaningservicesapp.data.local.database.entities.ServiceAddonEntity
import com.example.titossycleaningservicesapp.data.local.database.entities.ServiceEntity
import com.example.titossycleaningservicesapp.data.local.database.entities.SupplierEntity

@Database(
    entities = [
        CustomerEntity::class,
        SupplierEntity::class,
        EmployeeEntity::class,
        ServiceEntity::class,
        ServiceAddonEntity::class
    ],
    version = 11,
    exportSchema = false
)
@TypeConverters(Converters::class)
abstract class TitossyDatabase : RoomDatabase() {
    abstract fun customerDao(): CustomerDao
    abstract fun supplierDao(): SupplierDao
    abstract fun employeeDao(): EmployeeDao
    abstract fun serviceDao(): ServiceDao
}
