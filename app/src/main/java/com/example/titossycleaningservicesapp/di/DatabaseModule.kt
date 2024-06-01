package com.example.titossycleaningservicesapp.di

import android.content.Context
import android.util.Log
import androidx.room.Room
import com.example.titossycleaningservicesapp.data.local.database.db.TitossyDatabase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {

    @Provides
    @Singleton
    fun provideRoomDatabase(@ApplicationContext context: Context): TitossyDatabase {
        return Room.databaseBuilder(
            context,
            TitossyDatabase::class.java,
            "Titossy_db"
        ).setQueryCallback({ sqlQuery, bindArgs ->
            Log.d("RoomSql", "SQL Query: $sqlQuery SQL Args: $bindArgs")
        }, Runnable::run)
            .build()
    }

    @Provides
    fun provideCustomerDao(database: TitossyDatabase) = database.customerDao()

    @Provides
    fun provideEmployeeDao(database: TitossyDatabase) = database.employeeDao()

    @Provides
    fun provideSupplierDao(database: TitossyDatabase) = database.supplierDao()

    @Provides
    fun provideAddressDao(database: TitossyDatabase) = database.addressDao()

    @Provides
    fun provideServiceDao(database: TitossyDatabase) = database.serviceDao()
}