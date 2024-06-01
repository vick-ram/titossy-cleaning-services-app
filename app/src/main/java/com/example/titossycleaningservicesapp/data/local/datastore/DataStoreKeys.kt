package com.example.titossycleaningservicesapp.data.local.datastore

import androidx.datastore.core.DataStore
import androidx.datastore.core.IOException
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.emptyPreferences
import androidx.datastore.preferences.core.stringPreferencesKey
import com.example.titossycleaningservicesapp.di.Constants.LOGIN_KEY
import com.example.titossycleaningservicesapp.di.Constants.ONBOARDING_KEY
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class DataStoreKeys @Inject constructor(private val dataStore: DataStore<Preferences>) {

    companion object {
        private val ONBOARDING_COMPLETED = booleanPreferencesKey(ONBOARDING_KEY)
        private val USER_TYPE = stringPreferencesKey("userType")
    }

    suspend fun saveTokenToDataStore(token: String) {
        dataStore.edit { key ->
            key[stringPreferencesKey(LOGIN_KEY)] = token
        }
    }

    suspend fun getTokenFromDataStore(): String? {
        val prefs = dataStore.data.first()
        return prefs[stringPreferencesKey(LOGIN_KEY)]
    }

    suspend fun clearToken() {
        dataStore.edit { pref ->
            pref.remove(stringPreferencesKey(LOGIN_KEY))
        }
    }

    suspend fun setOnBoardingCompleted() {
        dataStore.edit { prefs ->
            prefs[ONBOARDING_COMPLETED] = true
        }
    }

    fun isOnBoardingCompleted(): Flow<Boolean> {
        return dataStore.data
            .catch { exception ->
                if (exception is IOException) {
                    emit(emptyPreferences())
                } else {
                    throw exception
                }
            }.map { value: Preferences ->
                value[ONBOARDING_COMPLETED] ?: false
            }

    }

    // Function to save user type
    suspend fun saveUserTypeToDataStore(userType: String) {
        dataStore.edit { key ->
            key[USER_TYPE] = userType
        }
    }

    // Function to get user type
    suspend fun getUserTypeFromDataStore(): String? {
        val prefs = dataStore.data.first()
        return prefs[USER_TYPE]
    }

}