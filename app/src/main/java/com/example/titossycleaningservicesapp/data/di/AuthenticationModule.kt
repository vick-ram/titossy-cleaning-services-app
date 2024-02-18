package com.example.titossycleaningservicesapp.data.di

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import com.example.titossycleaningservicesapp.data.di.Constants.dataStore
import com.example.titossycleaningservicesapp.data.repo.AuthenticationRepository
import com.example.titossycleaningservicesapp.data.repo.EmployeeRepository
import com.example.titossycleaningservicesapp.domain.repository.AuthenticationRepositoryImpl
import com.example.titossycleaningservicesapp.domain.repository.EmployeeRepositoryImpl
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.firestore
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AuthenticationModule {

    @Provides
    @Singleton
    fun provideFirebaseAuth(): FirebaseAuth {
        return FirebaseAuth.getInstance()
    }

    @Provides
    @Singleton
    fun provideAuthRepo(
        fireStore: FirebaseFirestore,
        firebaseAuth: FirebaseAuth
    ): AuthenticationRepository {
        return AuthenticationRepositoryImpl(fireStore, firebaseAuth)
    }

    @Provides
    @Singleton
    fun provideFireStore() = Firebase.firestore

    @Provides
    @Singleton
    fun providePrefs(@ApplicationContext context: Context): DataStore<Preferences> {
        return context.dataStore
    }

    @Provides
    @Singleton
    fun provideEmployeeFirebaseRepo(
        fireStore: FirebaseFirestore,
        firebaseAuth: FirebaseAuth
    ): EmployeeRepository {
        return EmployeeRepositoryImpl(fireStore, firebaseAuth)
    }

}